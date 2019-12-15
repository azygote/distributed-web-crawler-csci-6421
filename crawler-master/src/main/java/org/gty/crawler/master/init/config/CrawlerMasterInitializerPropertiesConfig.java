package org.gty.crawler.master.init.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CrawlerMasterInitializerProperties.class)
public class CrawlerMasterInitializerPropertiesConfig {
}
