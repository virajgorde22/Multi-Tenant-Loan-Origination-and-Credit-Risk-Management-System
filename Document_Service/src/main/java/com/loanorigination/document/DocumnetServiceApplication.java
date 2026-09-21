package com.loanorigination.document;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DocumnetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumnetServiceApplication.class, args);
	}

}
