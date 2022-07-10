package com.projectpubsub.publisherA;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
@Slf4j
public class PublisherAApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		log.info("Date in UTC: {}", LocalDateTime.now());
		SpringApplication.run(PublisherAApplication.class, args);
	}

}
