package com.telephone.directory.service_contact_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceContactManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceContactManagementApplication.class, args);
	}

}
