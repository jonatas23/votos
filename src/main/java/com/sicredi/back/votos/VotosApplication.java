package com.sicredi.back.votos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class VotosApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotosApplication.class, args);
    }

}
