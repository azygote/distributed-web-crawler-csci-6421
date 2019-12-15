package org.gty.crawler.slave.init.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("crawler")
public class CrawlerSlaveInitializerProperties {

    private final String masterAddress;
    private final String masterPort;

    private final String slaveName;
    private final String slaveAddress;
    private final String slavePort;

    private final String rabbitUri;
    private final String rabbitQueueName;
    private final String mongoDbUri;
    private final String mongoDbCollectionName;

    public CrawlerSlaveInitializerProperties(String masterAddress, String masterPort, String slaveName, String slaveAddress, String slavePort, String rabbitUri, String rabbitQueueName, String mongoDbUri, String mongoDbCollectionName) {
        this.masterAddress = masterAddress;
        this.masterPort = masterPort;
        this.slaveName = slaveName;
        this.slaveAddress = slaveAddress;
        this.slavePort = slavePort;
        this.rabbitUri = rabbitUri;
        this.rabbitQueueName = rabbitQueueName;
        this.mongoDbUri = mongoDbUri;
        this.mongoDbCollectionName = mongoDbCollectionName;
    }

    public String getMasterAddress() {
        return masterAddress;
    }

    public String getMasterPort() {
        return masterPort;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public String getSlaveAddress() {
        return slaveAddress;
    }

    public String getSlavePort() {
        return slavePort;
    }

    public String getRabbitUri() {
        return rabbitUri;
    }

    public String getRabbitQueueName() {
        return rabbitQueueName;
    }

    public String getMongoDbUri() {
        return mongoDbUri;
    }

    public String getMongoDbCollectionName() {
        return mongoDbCollectionName;
    }
}
