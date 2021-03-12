package com.youramaryllis.domainModel.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DomainModelApp implements CommandLineRunner
{
    public static void main( String[] args )
    {
        SpringApplication.run(DomainModelApp.class, args);
    }
    @Override
    public void run(String... args) throws Exception {}
}
