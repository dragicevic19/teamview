package org.example;

import org.example.controller.GetProjectsController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({GetProjectsController.class})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}