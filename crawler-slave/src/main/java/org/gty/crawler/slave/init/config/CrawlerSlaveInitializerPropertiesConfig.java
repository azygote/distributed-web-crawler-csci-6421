package org.gty.crawler.slave.init.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CrawlerSlaveInitializerProperties.class)
public class CrawlerSlaveInitializerPropertiesConfig {
}
