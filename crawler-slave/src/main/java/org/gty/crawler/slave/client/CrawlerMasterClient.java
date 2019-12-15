package org.gty.crawler.slave.client;

import org.gty.crawler.slave.model.Slave;
import org.gty.crawler.slave.model.Master;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class CrawlerMasterClient {

    private static final String HTTP = "http://";
    private static final String SEMICOLON = ":";
    private static final String PATH_PREFIX = "/api";

    private static final String REGISTER_NODE_PATH = PATH_PREFIX + "/register-node";

    private final RestTemplate restTemplate;

    public CrawlerMasterClient(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public Master registerToMaster(Master master, Slave slave) {
        URI uri = UriComponentsBuilder.fromHttpUrl(buildUrlFromNode(master, REGISTER_NODE_PATH))
            .encode(StandardCharsets.UTF_8)
            .build(true)
            .toUri();

        return restTemplate.postForObject(uri, slave, Master.class);
    }

    private static String buildUrlFromNode(Master master, String path) {
        return buildUrl(master.getMasterAddress(), Integer.parseInt(master.getMasterPort()), path);
    }

    private static String buildUrl(String address, int port, String path) {
        return HTTP + address + SEMICOLON + port + path;
    }
}
