package com.yves.webmagiccrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebmagicCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebmagicCrawlerApplication.class, args);
    }

}
