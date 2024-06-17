package com.popcorncafe.storeservice.repository.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Cart(UUID cartId, long clientId, UUID storeId, Instant orderDate, float orderPrice, Status status,
                   List<Product> products) implements Model {

    public enum Status {
        CREATED, PAID, IS_PREPARING, IS_READY, COMPLETE
    }
}
