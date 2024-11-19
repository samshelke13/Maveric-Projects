package com.maveric.mentee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


@OpenAPIDefinition(
		info = @Info(
				title = "Talent Share Portal",
				description = "Swagger configuration for Talent Share Portal",
				version = "1.0"
		)
)

//http://localhost:8080/swagger-ui/index.html
@SpringBootApplication
@SpringBootConfiguration
public class TalentShareMentorRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalentShareMentorRestServiceApplication.class, args);
	}

}
