package com.example.hotelbookingmanagement.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {
    @Bean(name = "threadPoolTaskExecutor")
    public AsyncTaskExecutor executor() {
        log.info("Creating Async Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setAwaitTerminationSeconds(10);
        executor.setThreadNamePrefix("Thread-");
        executor.setAllowCoreThreadTimeOut(true);
        return executor;
    }
}
