package com.project.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
@Slf4j
public class PubSubApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		log.info("Date in UTC: {}", LocalDateTime.now());
		SpringApplication.run(PubSubApplication.class, args);
	}

}
