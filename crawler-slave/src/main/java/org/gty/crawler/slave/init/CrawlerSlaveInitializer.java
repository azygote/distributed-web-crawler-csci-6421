package org.gty.crawler.slave.init;

import org.gty.crawler.slave.service.CrawlerSlaveService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CrawlerSlaveInitializer {

    private final CrawlerSlaveService crawlerSlaveService;

    public CrawlerSlaveInitializer(CrawlerSlaveService crawlerSlaveService) {
        this.crawlerSlaveService = crawlerSlaveService;
    }

    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        crawlerSlaveService.registerToMaster();
    }
}
