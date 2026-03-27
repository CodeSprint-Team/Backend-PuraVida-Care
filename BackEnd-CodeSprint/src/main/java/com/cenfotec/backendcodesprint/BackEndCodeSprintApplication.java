package com.cenfotec.backendcodesprint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.cenfotec.backendcodesprint.api",
        "com.cenfotec.backendcodesprint.logic",
        "com.cenfotec.backendcodesprint.Config",
        "com.cenfotec.backendcodesprint.Security"
})
public class BackEndCodeSprintApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackEndCodeSprintApplication.class, args);
    }
}