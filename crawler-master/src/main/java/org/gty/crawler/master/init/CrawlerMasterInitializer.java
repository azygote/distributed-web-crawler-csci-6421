package org.gty.crawler.master.init;

import org.gty.crawler.master.init.config.CrawlerMasterInitializerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class CrawlerMasterInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerMasterInitializer.class);

    private final CrawlerMasterInitializerProperties properties;
    private final MongoTemplate mongoTemplate;

    public CrawlerMasterInitializer(CrawlerMasterInitializerProperties properties, MongoTemplate mongoTemplate) {
        this.properties = properties;
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        initializeMongoDb();
    }

    private void initializeMongoDb() {
        String collectionName = properties.getMongoDbCollectionName();

        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
    }
}
