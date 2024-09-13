package com.carbonhater.co2zerobookmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class Co2ZeroBookmarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(Co2ZeroBookmarkApplication.class, args);
    }

}
