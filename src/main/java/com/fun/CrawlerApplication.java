package com.fun;

import com.fun.listener.CrawlerStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CrawlerApplication.class);
        springApplication.addListeners(new CrawlerStartup());
        springApplication.run(args);
    }
}
