package io.github.artemnefedov.interfaces.eventhandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artemnefedov.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopcorncafeListener {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "clients",
            groupId = "groupId"
    )
    void listenerClients(String json) throws JsonProcessingException {

        System.out.println("Client topic: " + objectMapper.readValue(json, Cart.class) + "\n\n\n");
    }

    @KafkaListener(
            topics = "kitchen",
            groupId = "groupId"
    )
    void listenerKitchen(String json) throws JsonProcessingException {

        System.out.println("Kitchen topic: " + objectMapper.readValue(json, Cart.class) + "\n\n\n");
    }
//
//    @KafkaListener(
//            topics = "scoreboard",
//            groupId = "groupId"
//    )
//    void listenerScoreboard(String json) throws JsonProcessingException {
//
//        System.out.println("Scoreboard topic: " + objectMapper.readValue(json, Cart.class) + "\n\n\n");
//    }

    @KafkaListener(
            topics = "cashier",
            groupId = "groupId"
    )
    void listenerCashier(String json) throws JsonProcessingException {

        System.out.println("Cashier topic: " + objectMapper.readValue(json, Cart.class) + "\n\n\n");
    }
}
