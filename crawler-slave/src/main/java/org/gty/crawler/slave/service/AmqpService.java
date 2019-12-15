package org.gty.crawler.slave.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AmqpService {

    private static final Logger logger = LoggerFactory.getLogger(AmqpService.class);

    private final AmqpTemplate amqpTemplate;
    private final String queueName;

    public AmqpService(AmqpTemplate amqpTemplate,
                       @Value("${crawler.rabbit-queue-name}") String queueName) {
        this.amqpTemplate = amqpTemplate;
        this.queueName = queueName;
    }

    public void addUrls(Set<String> urls) {
        urls.forEach(url -> amqpTemplate.convertAndSend(queueName, url));
    }
}
