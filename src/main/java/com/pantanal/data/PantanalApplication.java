package com.pantanal.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {HypermediaAutoConfiguration.class})
@EnableScheduling
public class PantanalApplication {
    public static void main(String args[]){
        SpringApplication.run(PantanalApplication.class , args);
    }
}
