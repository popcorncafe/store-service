package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.IngredientDao;
import com.popcorncafe.storeservice.dao.model.Ingredient;
import com.popcorncafe.storeservice.dto.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IngredientDaoImpl implements IngredientDao {
    @Override
    public Optional<Ingredient> getById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Ingredient> getAll(Page page) {
        return null;
    }

    @Override
    public UUID save(Ingredient model) {
        return null;
    }

    @Override
    public boolean update(Ingredient model) {
        return false;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public Optional<Ingredient> findByStoreId(UUID id) {
        return Optional.empty();
    }
}
