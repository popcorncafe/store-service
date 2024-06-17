package com.popcorncafe.storeservice.service;

import com.popcorncafe.storeservice.service.dto.IngredientDto;
import com.popcorncafe.storeservice.service.dto.Page;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IngredientService {

    List<IngredientDto> getIngredients(Page page);

    IngredientDto getIngredient(UUID id);

    IngredientDto createIngredient(IngredientDto ingredientDto);

    boolean updateIngredient(IngredientDto ingredientDto);

    boolean deleteIngredient(UUID id);

    Map<IngredientDto, Float> getByStore(UUID id);

    boolean updateAmountByStore(Map<UUID, Float> amount, UUID id);
}
