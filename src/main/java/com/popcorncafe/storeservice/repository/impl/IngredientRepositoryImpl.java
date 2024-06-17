package com.popcorncafe.storeservice.repository.impl;

import com.popcorncafe.storeservice.repository.IngredientRepository;
import com.popcorncafe.storeservice.repository.model.Ingredient;
import com.popcorncafe.storeservice.repository.rowMapper.IngredientMapper;
import com.popcorncafe.storeservice.service.dto.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class IngredientRepositoryImpl implements IngredientRepository {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(IngredientRepositoryImpl.class);

    public IngredientRepositoryImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    @Cacheable(value = "IngredientRepository::get", key = "#id")
    public Optional<Ingredient> get(UUID id) {

        log.debug("Getting ingredient with id: {}", id);

        return parameterJdbcTemplate.query("""
                        SELECT ingredient_id, name, unit_price, measure
                        FROM ingredient
                        WHERE ingredient_id=:ingredient_id
                        LIMIT 1;
                        """, Map.of("ingredient_id", id), new IngredientMapper()).stream()
                .findFirst();
    }

    @Override
    @Cacheable(value = "IngredientRepository::getAll", key = "#page.hashCode()")

    public List<Ingredient> getAll(Page page) {

        log.debug("Getting all ingredients from db");

        return parameterJdbcTemplate.query("""
                        SELECT ingredient_id, name, unit_price, measure
                        FROM ingredient
                        LIMIT :page_size
                        OFFSET :page_offset;
                        """, Map.of("page_size", page.size(), "page_offset", page.offset()),
                new IngredientMapper()
        );
    }

    @Override
    @CacheEvict(value = "IngredientRepository::getAll", allEntries = true)
    public UUID create(Ingredient ingredient) {

        log.debug("Creating ingredient");

        return parameterJdbcTemplate.queryForObject("""
                INSERT INTO ingredient(name, unit_price, measure)
                VALUES (:name, :unit_price, :measure)
                RETURNING ingredient_id;
                """, Map.of("name", ingredient.name(), "unit_price",
                ingredient.unitPrice(), "measure",
                ingredient.measure().name()
        ), UUID.class);
    }

    @Override
    @Caching(evict = @CacheEvict(value = "IngredientRepository::getAll", allEntries = true), put = @CachePut(value = "IngredientRepository::get", key = "#ingredient.ingredientId()"))
    public boolean update(Ingredient ingredient) {

        log.debug("Updating ingredient with id: {}", ingredient.ingredientId());

        return parameterJdbcTemplate.update("""
                UPDATE ingredient
                SET name=:name,
                    unit_price=:unit_price,
                    measure=:measure
                WHERE ingredient_id=:ingredient_id;                                
                """, Map.of("name", ingredient.name(), "unit_price", ingredient.unitPrice(),
                "measure", ingredient.measure().name(), "ingredient_id",
                ingredient.ingredientId()
        )) >= 1;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "IngredientRepository::get", key = "#id"),
            @CacheEvict(value = "IngredientRepository::getAll", allEntries = true)})
    public boolean delete(UUID id) {
        return parameterJdbcTemplate.update("DELETE FROM ingredient WHERE ingredient_id=:ingredient_id;",
                Map.of("ingredient_id", id)
        ) >= 1;
    }

    @Override
    public Map<UUID, Float> getByStore(UUID id) {

        return parameterJdbcTemplate.query("""
                        SELECT ingredient_id, amount
                        FROM store_ingredient
                        WHERE store_id=:store_id;
                        """, Map.of("store_id", id),
                (rs, rowNum) -> Map.entry(UUID.fromString(rs.getString("ingredient_id")),
                        rs.getFloat("amount")
                )
        ).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public boolean updateAmountByStore(Map<UUID, Float> amount, UUID id) {
//        return parameterJdbcTemplate.update("""
//                INSERT INTO store_ingredient(store_id, ingredient_id, amount)
//                VALUES (:store_id, :ingredient_id, :amount)
//                ON CONFLICT (store_id, ingredient_id) DO UPDATE
//                SET amount=:amount;
//                """,
//                amount.entrySet().stream()
//                        .map(entry -> Map.of("store_id", id, "ingredient_id", entry.getKey(), "amount", entry.getValue()))
//                        .toArray(Map[]::new)
//        ).length == amount.size(); 
        return parameterJdbcTemplate.batchUpdate("""
                INSERT INTO store_ingredient(store_id, ingredient_id, amount)
                VALUES (:store_id, :ingredient_id, :amount)
                ON CONFLICT (store_id, ingredient_id) DO UPDATE
                SET amount=:amount;
                """, amount.entrySet().stream()
                .map(entry -> Map.of("store_id", id, "ingredient_id",
                        entry.getKey(), "amount",
                        entry.getValue()
                )).toArray(Map[]::new)).length == amount.size();
    }
}
