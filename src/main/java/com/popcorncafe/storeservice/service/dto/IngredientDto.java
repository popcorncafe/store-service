package com.popcorncafe.storeservice.service.dto;

import java.util.UUID;

public record IngredientDto(
        UUID id,
        String name,
        float unitPrice,
        String measure
) implements Dto {
}
