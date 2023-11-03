package com.popcorncafe.storeservice.dao.model;

import java.util.Map;
import java.util.UUID;

public record Product(
        UUID productId,
        String name,
        String description,
        Size size,
        Map<Ingredient, Float> ingredientAmount

) implements Model{
    public enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }
}
