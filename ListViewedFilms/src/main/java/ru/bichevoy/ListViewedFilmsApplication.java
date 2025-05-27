package ru.bichevoy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ListViewedFilmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListViewedFilmsApplication.class, args);
    }
}
