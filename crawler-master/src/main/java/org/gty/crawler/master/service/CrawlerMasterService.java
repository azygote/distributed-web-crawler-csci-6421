package org.gty.crawler.master.service;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.gty.crawler.master.client.CrawlerSlaveClient;
import org.gty.crawler.master.init.config.CrawlerMasterInitializerProperties;
import org.gty.crawler.master.model.Master;
import org.gty.crawler.master.model.SeedUrl;
import org.gty.crawler.master.model.Slave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.ConnectException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CrawlerMasterService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerMasterService.class);
    private static final String RABBIT_LISTENER_ID = "RABBIT_LISTENER_ID";

    private final Executor masterExecutor;
    private final CrawlerMasterInitializerProperties properties;
    private final AmqpService amqpService;
    private final RabbitListenerEndpointRegistry registry;
    private final CrawlerSlaveClient client;
    private final List<Slave> slaveList;
    private final AtomicReference<SeedUrl> seedUrl;
    private final AtomicBoolean seedUrlAlreadyEnqueued;

    public CrawlerMasterService(Executor masterExecutor,
                                CrawlerMasterInitializerProperties properties,
                                AmqpService amqpService,
                                RabbitListenerEndpointRegistry registry,
                                CrawlerSlaveClient client) {
        this.masterExecutor = masterExecutor;

        this.properties = properties;
        this.amqpService = amqpService;
        this.registry = registry;
        this.client = client;

        slaveList = new CopyOnWriteArrayList<>();
        seedUrl = new AtomicReference<>();
        seedUrlAlreadyEnqueued = new AtomicBoolean(false);
    }

    public Master registerNode(Slave node) {
        slaveList.add(node);

        Master master = new Master();
        master.setMasterAddress(properties.getMasterAddress());
        master.setMasterPort(properties.getMasterPort());

        return master;
    }

    public void assignSeed(SeedUrl seedUrl) {
        this.seedUrl.set(seedUrl);
    }

    public void start() {
        if (!seedUrlAlreadyEnqueued.get()) {
            amqpService.addUrl(seedUrl.get().getUrl());
            seedUrlAlreadyEnqueued.set(true);
        }
        registry.getListenerContainer(RABBIT_LISTENER_ID).start();
    }

    public void stop() {
        CompletableFuture.runAsync(() -> registry.getListenerContainer(RABBIT_LISTENER_ID).stop(), masterExecutor);
    }

    public Map<String, Slave> getSlaves() {
        Map<String, Slave> map = new HashMap<>();
        for (int i = 0; i < slaveList.size(); i++) {
            map.put(String.valueOf(i), slaveList.get(i));
        }
        return map;
    }

    @RabbitListener(id = RABBIT_LISTENER_ID,
        queues = "${crawler.rabbit-queue-name}",
        autoStartup = "false",
        executor = "masterExecutor")
    public void listenOnQueue(String url) {
        if (!slaveList.isEmpty()) {
            int slaveIndex = distribute(url);
            Slave slave = slaveList.get(slaveIndex);

            waitFor500ms();
            logger.info("distribute url [{}] to node [{}]{}", url, slaveIndex, slave);
            client.crawl(slave, url);
        }
    }

    private void waitFor500ms() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int distribute(String url) {
        MessageDigest digest = DigestUtils.getSha256Digest();
        byte[] sha256 = DigestUtils.digest(digest, StringUtils.getBytesUtf8(url));
        return new BigInteger(sha256).mod(BigInteger.valueOf(slaveList.size())).intValue();
    }

    @Scheduled(fixedRate = 500L)
    public void updateSlaveList() {
        slaveList.forEach(slave -> {
            try {
                client.healthCheck(slave);
            } catch (Exception ex) {
                if (ex.getCause() instanceof ConnectException) {
                    logger.info("Slave [{}] is offline", slave);
                    slaveList.remove(slave);
                }
            }
        });
    }
}
