package io.github.artemnefedov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artemnefedov.entity.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(String topic, String key, Cart value) {
        try {
            var future = kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(value));

            future.whenComplete((sendResult, exception) -> {
                if (exception != null) {
                    future.completeExceptionally(exception);
                } else {
                    future.complete(sendResult);
                }
            });
        } catch (JsonProcessingException exception) {
            log.error(exception.getMessage());
        }
    }

}
