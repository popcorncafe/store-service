package com.popcorncafe.storeservice.service.mapper;

import com.popcorncafe.storeservice.repository.model.Ingredient;
import com.popcorncafe.storeservice.service.dto.IngredientDto;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper implements Mapper<Ingredient, IngredientDto> {
    @Override
    public Ingredient toModel(IngredientDto dto) {
        return new Ingredient(
                dto.id(),
                dto.name(),
                dto.unitPrice(),
                Ingredient.Measure.valueOf(dto.measure())
        );
    }

    @Override
    public IngredientDto toDto(Ingredient model) {
        return new IngredientDto(
                model.ingredientId(),
                model.name(),
                model.unitPrice(),
                model.measure().name()
        );
    }
}
