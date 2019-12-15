package org.gty.crawler.slave.service;

import org.gty.crawler.slave.client.CrawlerMasterClient;
import org.gty.crawler.slave.client.DownloadClient;
import org.gty.crawler.slave.init.config.CrawlerSlaveInitializerProperties;
import org.gty.crawler.slave.model.Master;
import org.gty.crawler.slave.model.Record;
import org.gty.crawler.slave.model.Slave;
import org.gty.crawler.slave.repository.RecordRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CrawlerSlaveService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerSlaveService.class);

    private final CrawlerMasterClient client;
    private final DownloadClient downloadClient;
    private final CrawlerSlaveInitializerProperties properties;
    private final AtomicReference<Master> master;
    private final RecordRepository repository;
    private final Executor slaveExecutor;
    private final AmqpService amqpService;
    private final AtomicLong counter;

    public CrawlerSlaveService(CrawlerMasterClient client,
                               DownloadClient downloadClient,
                               CrawlerSlaveInitializerProperties properties,
                               RecordRepository repository,
                               Executor slaveExecutor,
                               AmqpService amqpService) {
        this.client = client;
        this.downloadClient = downloadClient;
        this.properties = properties;
        this.repository = repository;
        this.slaveExecutor = slaveExecutor;
        this.amqpService = amqpService;

        master = new AtomicReference<>();
        counter = new AtomicLong(0L);
    }

    public void registerToMaster() {
        Master master = new Master();
        master.setMasterAddress(properties.getMasterAddress());
        master.setMasterPort(properties.getMasterPort());

        Slave slave = new Slave();
        slave.setNodeName(properties.getSlaveName());
        slave.setNodeAddress(properties.getSlaveAddress());
        slave.setNodePort(properties.getSlavePort());

        master = client.registerToMaster(master, slave);
        this.master.set(master);
    }

    public void crawl(String url) {
        CompletableFuture.runAsync(() -> crawlHandler(url), slaveExecutor)
            .exceptionallyAsync(throwable -> {
                logger.error("", throwable);
                return null;
            }, slaveExecutor);
    }

    public long assignedUrlCount() {
        return counter.get();
    }

    @Transactional(transactionManager = "mongoTransactionManager", rollbackFor = Throwable.class)
    public void crawlHandler(String url) {
        counter.incrementAndGet();

        if (!urlExists(url)) {
            String content = downloadClient.download(url);

            addToMongo(url, content);

            Set<String> urls = extractNewUrls(url, content);

            amqpService.addUrls(urls);
        }
    }

    private boolean urlExists(String url) {
        return !repository.findByUrl(url).isEmpty();
    }

    private void addToMongo(String url, String content) {
        Record record = new Record();
        record.setUrl(url);
        record.setContent(content);

        repository.save(record);

        logger.info("Persisted url [{}]", url);
    }

    private Set<String> extractNewUrls(String url, String content) {
        Document document = Jsoup.parse(content, url);

        return document.select("a")
            .stream()
            .map(link -> link.attr("abs:href"))
            .collect(Collectors.toUnmodifiableSet());
    }

    public Master getMaster() {
        return master.get();
    }

}
