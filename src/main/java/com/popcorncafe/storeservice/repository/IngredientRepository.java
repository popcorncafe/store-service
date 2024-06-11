package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.model.Ingredient;

import java.util.Map;
import java.util.UUID;

public interface IngredientRepository extends AbstractRepository<Ingredient> {
    Map<UUID, Float> getByStore(UUID id);

    boolean updateAmountByStore(Map<UUID, Float> amount, UUID id);
}
