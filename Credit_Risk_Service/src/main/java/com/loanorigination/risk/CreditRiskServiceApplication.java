package com.loanorigination.risk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CreditRiskServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditRiskServiceApplication.class, args);
	}

}
