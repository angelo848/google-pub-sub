package com.project.pubsub.controller;

import com.project.pubsub.configs.pubsub.PubSubPublisher;
import com.project.pubsub.model.schema.TestSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @Autowired
    private PubSubPublisher topicAProducer;

    @PostMapping("send-message")
    ResponseEntity<Void> sendMessageTest(@RequestBody TestSchema schema) {
        topicAProducer.send(schema);
        return ResponseEntity.noContent().build();

    }
}
