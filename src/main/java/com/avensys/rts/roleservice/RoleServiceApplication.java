package com.avensys.rts.roleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RoleServiceApplication{
    public static void main(String[] args) {
        SpringApplication.run(RoleServiceApplication.class,args);

    }
}