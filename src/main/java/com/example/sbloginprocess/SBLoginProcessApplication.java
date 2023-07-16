package com.example.sbloginprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.example.sbloginprocess")
@EnableJpaRepositories("com.example.sbloginprocess.repository")
@EntityScan("com.example.sbloginprocess.model")
public class SBLoginProcessApplication {

    public static void main(String[] args) {
        SpringApplication.run(SBLoginProcessApplication.class, args);
    }
}
