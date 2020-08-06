package com.example.demo.concurrent;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SimpleExecutorConfig {

    @Bean("simpleExecutor")
    public ExecutorService  fixedExecutorPool(){
        ExecutorService  pool = Executors.newFixedThreadPool(20);

        return pool;
    }



}
