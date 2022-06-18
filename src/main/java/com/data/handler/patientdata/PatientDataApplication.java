package com.data.handler.patientdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PatientDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientDataApplication.class, args);
	}

}
