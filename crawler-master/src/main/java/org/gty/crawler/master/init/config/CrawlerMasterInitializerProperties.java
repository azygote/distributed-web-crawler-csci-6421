package org.gty.crawler.master.init.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("crawler")
public class CrawlerMasterInitializerProperties {

    private final String masterAddress;
    private final String masterPort;

    private final String rabbitUri;
    private final String rabbitQueueName;
    private final String mongoDbUri;
    private final String mongoDbCollectionName;

    public CrawlerMasterInitializerProperties(String masterAddress, String masterPort, String rabbitUri, String rabbitQueueName, String mongoDbUri, String mongoDbCollectionName) {
        this.masterAddress = masterAddress;
        this.masterPort = masterPort;
        this.rabbitUri = rabbitUri;
        this.rabbitQueueName = rabbitQueueName;
        this.mongoDbUri = mongoDbUri;
        this.mongoDbCollectionName = mongoDbCollectionName;
    }


    public String getMongoDbUri() {
        return mongoDbUri;
    }

    public String getMongoDbCollectionName() {
        return mongoDbCollectionName;
    }

    public String getMasterAddress() {
        return masterAddress;
    }

    public String getMasterPort() {
        return masterPort;
    }

    public String getRabbitUri() {
        return rabbitUri;
    }

    public String getRabbitQueueName() {
        return rabbitQueueName;
    }
}
