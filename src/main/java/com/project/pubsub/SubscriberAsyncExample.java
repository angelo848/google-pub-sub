package com.project.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.project.pubsub.model.schema.TestSchema;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SubscriberAsyncExample {

    private final ObjectMapper objectMapper;

    public static void main(String... args) {
        // TODO(developer): Replace these variables before running the sample.
        String projectId = "charming-sonar-359523";
        String subscriptionId = "topic-a-sub";

        subscribeAsyncExample(projectId, subscriptionId);
    }

    public static void subscribeAsyncExample(String projectId, String subscriptionId) {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);

        // Instantiate an asynchronous message receiver.
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    // Handle incoming message, then ack the received message.
                    System.out.println("Id: " + message.getMessageId());
                    System.out.println("Data: " + message.getData().toStringUtf8());

                    TestSchema response = new TestSchema();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        response = mapper.readValue(new String(message.getData().toByteArray(), StandardCharsets.UTF_8), TestSchema.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    if (response.getMessage().equals("throw error")) {
                        consumer.nack();
                        return;
                    }
                    consumer.ack();
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning();
            System.out.printf("Listening for messages on %s:\n", subscriptionName);
            // Allow the subscriber to run for 10s unless an unrecoverable error occurs.
            subscriber.awaitTerminated(10, TimeUnit.SECONDS);
        } catch (TimeoutException timeoutException) {
            // Shut down the subscriber after 10s. Stop receiving messages.
            subscriber.stopAsync();
        }
    }
}
