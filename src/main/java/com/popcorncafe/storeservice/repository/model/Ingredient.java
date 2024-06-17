package com.popcorncafe.storeservice.repository.model;

import java.util.UUID;

public record Ingredient(UUID ingredientId, String name, float unitPrice, Measure measure) implements Model {

    public enum Measure {
        GRAMS, KILOGRAMS, MILLILITERS, LITERS
    }
}
