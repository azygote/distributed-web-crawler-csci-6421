package org.gty.crawler.slave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlerSlaveApp {

    public static void main(String[] args) {
        System.setProperty("log4j2.contextSelector",
            "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");

        SpringApplication.run(CrawlerSlaveApp.class, args);
    }
}
