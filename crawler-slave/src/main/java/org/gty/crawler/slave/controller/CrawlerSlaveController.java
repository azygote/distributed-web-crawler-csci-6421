package org.gty.crawler.slave.controller;

import org.gty.crawler.slave.model.CrawlUrl;
import org.gty.crawler.slave.service.CrawlerSlaveService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CrawlerSlaveController {

    private final CrawlerSlaveService service;

    public CrawlerSlaveController(CrawlerSlaveService service) {
        this.service = service;
    }

    @PostMapping(value = "/api/crawl", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void crawl(@RequestBody CrawlUrl crawlUrl) {
        service.crawl(crawlUrl.getUrl());
    }

    @GetMapping(value = "/api/health-check", produces = MediaType.APPLICATION_JSON_VALUE)
    public void healthCheck() {
    }

    @GetMapping(value = "/api/assigned-url-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<?, ?> assignedUrlCount() {
        return Map.of("count", service.assignedUrlCount());
    }
}
