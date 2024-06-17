package com.popcorncafe.storeservice.service.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CartDto(UUID id, long clientId, UUID storeId, Instant orderDate, float orderPrice, String status,
                      List<UUID> products) implements Dto {
}
