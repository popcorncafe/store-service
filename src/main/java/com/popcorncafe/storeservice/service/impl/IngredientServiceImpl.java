package com.popcorncafe.storeservice.service.impl;

import com.popcorncafe.storeservice.repository.IngredientRepository;
import com.popcorncafe.storeservice.service.IngredientService;
import com.popcorncafe.storeservice.service.dto.IngredientDto;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.mapper.IngredientMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper mapper;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper mapper) {
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    @Override
    public List<IngredientDto> getIngredients(Page page) {
        return ingredientRepository.getAll(page).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public IngredientDto getIngredient(UUID id) {
        return ingredientRepository.get(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    @Override
    public IngredientDto createIngredient(IngredientDto ingredientDto) {
        var ingredientId = ingredientRepository.create(mapper.toModel(ingredientDto));
        return new IngredientDto(
                ingredientId,
                ingredientDto.name(),
                ingredientDto.unitPrice(),
                ingredientDto.measure()
        );
    }

    @Override
    public boolean updateIngredient(IngredientDto ingredientDto) {
        return ingredientRepository.update(mapper.toModel(ingredientDto));
    }

    @Override
    public boolean deleteIngredient(UUID id) {
        return ingredientRepository.delete(id);
    }

    @Override
    public Map<IngredientDto, Float> getByStore(UUID storeId) {
        Map<IngredientDto, Float> ingredientsAmount = new HashMap<>();
        Map<UUID, Float> idAmount = ingredientRepository.getByStore(storeId);

        idAmount.forEach((id, amount) -> {
            var ingredient = this.getIngredient(id);
            ingredientsAmount.put(ingredient, amount);
        });

        return ingredientsAmount;
    }

    @Override
    public boolean updateAmountByStore(Map<UUID, Float> amount, UUID id) {
        return ingredientRepository.updateAmountByStore(amount, id);
    }
}
