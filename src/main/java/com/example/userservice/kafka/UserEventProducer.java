package com.example.userservice.kafka;

import com.example.userservice.dto.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class UserEventProducer {

    private static final String USER_CREATED_TOPIC = "user_created";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public UserEventProducer(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper){
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserCreatedEvent(UserResponse userResponse) {

        Map<String, Object> event = Map.of(
                "eventType", "USER_CREATED",
                "userId", userResponse.getId(),
                "name", userResponse.getName(),
                "email", userResponse.getEmail(),
                "age", userResponse.getAge()
        );

        try {

            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(USER_CREATED_TOPIC, payload);

        } catch (JsonProcessingException ex) {

            throw new IllegalStateException(
                    "Failed to serialize user created event payload", ex
            );
        }
    }
}