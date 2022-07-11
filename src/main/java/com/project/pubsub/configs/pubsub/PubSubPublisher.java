package com.project.pubsub.configs.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public class PubSubPublisher {

    @Value("${google.pubsub.projectId}")
    private String projectId;

    @Autowired
    private ObjectMapper objectMapper;

    private final String topicId;

    public PubSubPublisher(String topicId) {
        if (!StringUtils.hasText(topicId)) {
            throw new IllegalArgumentException("The provided topic id is null!");
        }
        this.topicId = topicId;
    }

    public void send(Object data) {
        TopicName topicName = TopicName.of(projectId, topicId);

        Publisher publisher = null;
        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            String dataAsString = objectMapper.writeValueAsString(data);
            ByteString message = ByteString.copyFromUtf8(dataAsString);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(message).build();

            log.info("Sending message with content: {}", dataAsString);
            // Once published, returns a server-assigned message id (unique within the topic)
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            String messageId = messageIdFuture.get();
            log.info("Published message ID: {}", messageId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                try {
                    publisher.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
