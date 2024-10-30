package com.leungcheng.spring_simple_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@SpringBootApplication
public class SpringSimpleBackendApplication implements RepositoryRestConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SpringSimpleBackendApplication.class, args);
	}

	@Override
	public void configureValidatingRepositoryEventListener(
			ValidatingRepositoryEventListener v) {
		v.addValidator("beforeCreate", new RequestValidator());
	}
}
