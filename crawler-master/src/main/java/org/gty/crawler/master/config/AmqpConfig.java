package org.gty.crawler.master.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class AmqpConfig {

    @Bean
    public Queue crawlerQueue(@Value("${crawler.rabbit-queue-name}") String name) {
        return new Queue(name);
    }
}
