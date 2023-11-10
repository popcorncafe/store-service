package com.popcorncafe.storeservice.dao.model;

import java.util.UUID;

public record Ingredient(
        UUID ingredientId,
        String name,
        float unitPrice,
        Measure measure
) implements Model {
    public enum Measure {
        GRAMS,
        KILOGRAMS,
        MILLILITERS,
        LITERS
    }
}
