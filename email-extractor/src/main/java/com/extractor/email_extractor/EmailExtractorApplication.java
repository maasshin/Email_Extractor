package com.extractor.email_extractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Enables asynchronous method execution
public class EmailExtractorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailExtractorApplication.class, args);
	}

}
