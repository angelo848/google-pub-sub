package com.projectpubsub.publisherA.configs.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PubSubConfig {

    @Value("${google.pubsub.topicA}")
    private String topicA;

    @Bean("topicAProducer")
    public PubSubPublisher topicAPublisher() {
        return new PubSubPublisher(topicA);
    }
}
