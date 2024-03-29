package com.murphy1.serviced.serviced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ServicedApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicedApplication.class, args);
    }

}
