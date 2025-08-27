package com.ggamja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GgamjaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GgamjaApplication.class, args);
	}

}
