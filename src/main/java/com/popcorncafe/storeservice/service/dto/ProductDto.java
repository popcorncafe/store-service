package com.popcorncafe.storeservice.service.dto;

import java.util.UUID;

public record ProductDto(UUID id, String name, String description, String size) implements Dto {
}
