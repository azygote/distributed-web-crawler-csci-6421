package org.gty.crawler.master.client;

import org.gty.crawler.master.model.CrawlUrl;
import org.gty.crawler.master.model.Slave;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class CrawlerSlaveClient {

    private static final String HTTP = "http://";
    private static final String SEMICOLON = ":";
    private static final String PATH_PREFIX = "/api";

    private static final String CRAWL_PATH = PATH_PREFIX + "/crawl";
    private static final String HEALTH_CHECK_PATH = PATH_PREFIX + "/health-check";
    private static final String ASSIGNED_URL_COUNT = PATH_PREFIX + "/assigned-url-count";

    private final RestTemplate restTemplate;

    public CrawlerSlaveClient(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public void crawl(Slave slave, String url) {
        URI uri = UriComponentsBuilder.fromHttpUrl(buildUrlFromSlave(slave, CRAWL_PATH))
            .encode(StandardCharsets.UTF_8)
            .build(true)
            .toUri();

        CrawlUrl crawlUrl = new CrawlUrl();
        crawlUrl.setUrl(url);

        restTemplate.postForObject(uri, crawlUrl, String.class);
    }

    public void healthCheck(Slave slave) {
        URI uri = UriComponentsBuilder.fromHttpUrl(buildUrlFromSlave(slave, HEALTH_CHECK_PATH))
            .encode(StandardCharsets.UTF_8)
            .build(true)
            .toUri();

        restTemplate.getForObject(uri, String.class);
    }

    public Map<String, Long> getAssignedUrlCount(Slave slave) {
        URI uri = UriComponentsBuilder.fromHttpUrl(buildUrlFromSlave(slave, ASSIGNED_URL_COUNT))
            .encode(StandardCharsets.UTF_8)
            .build(true)
            .toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Long>>() {
        }).getBody();
    }

    private static String buildUrlFromSlave(Slave slave, String path) {
        return buildUrl(slave.getNodeAddress(), Integer.parseInt(slave.getNodePort()), path);
    }

    private static String buildUrl(String address, int port, String path) {
        return HTTP + address + SEMICOLON + port + path;
    }
}
