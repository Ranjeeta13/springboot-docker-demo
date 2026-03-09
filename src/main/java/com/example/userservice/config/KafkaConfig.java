package com.example.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    private static final String USER_TOPIC = "user_created";

    @Bean
    public NewTopic userCreatedTopic()  {
        return new NewTopic(USER_TOPIC, 1, (short) 1);
    }
}
