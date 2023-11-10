package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.IngredientDao;
import com.popcorncafe.storeservice.dao.model.Ingredient;
import com.popcorncafe.storeservice.dao.rowmapper.IngredientMapper;
import com.popcorncafe.storeservice.dto.Page;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IngredientDaoImpl implements IngredientDao {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public IngredientDaoImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    public Optional<Ingredient> getById(UUID id) {
        return parameterJdbcTemplate.query("""
                                SELECT ingredient_id, name, unit_price, measure
                                FROM ingredient
                                WHERE ingredient_id=:ingredient_id
                                LIMIT 1;
                                """,
                        Map.of("ingredient_id", id), new IngredientMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<Ingredient> getAll(Page page) {
        var sql = """
                SELECT cart_id, client_id, store_id, order_date, order_price, status
                FROM cart
                LIMIT :page_size
                OFFSET :page_offset;
                """;
        var params = new MapSqlParameterSource()
                .addValue("page_size", page.size())
                .addValue("page_offset", page.offset());
        return parameterJdbcTemplate.query(sql, params, new IngredientMapper());
    }

    @Override
    public UUID save(Ingredient ingredient) {
        var sql = """
                INSERT INTO ingredient(name, unit_price, measure)
                VALUES (:name, :unit_price, :measure)
                RETURNING ingredient_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("name", ingredient.name())
                .addValue("unit_price", ingredient.unitPrice())
                .addValue("measure", ingredient.measure().name());
        return parameterJdbcTemplate.queryForObject(sql, params, UUID.class);
    }

    @Override
    public boolean update(Ingredient ingredient) {
        var sql = """
                UPDATE ingredient
                SET name=:name,
                    unit_price=:unit_price,
                    measure=:measure
                WHERE ingredient_id=:ingredient_id;                                
                """;
        var params = new MapSqlParameterSource()
                .addValue("name", ingredient.name())
                .addValue("unit_price", ingredient.unitPrice())
                .addValue("measure", ingredient.measure().name());
        return parameterJdbcTemplate.update(sql, params) == 1;
    }

    @Override
    public boolean delete(UUID id) {
        return parameterJdbcTemplate.update(
                "DELETE FROM ingredient WHERE ingredient_id=:ingredient_id;",
                Map.of("ingredient_id", id)) == 1;
    }

    @Override
    public List<Ingredient> findByStoreId(UUID id, Page page) {
        var sql = """
                SELECT cart_id, client_id, store_id, order_date, order_price, status
                FROM cart
                WHERE store_id=:store_id
                LIMIT :page_size
                OFFSET :page_offset;
                """;
        var params = new MapSqlParameterSource()
                .addValue("store_id", id)
                .addValue("page_size", page.size())
                .addValue("page_offset", page.offset());
        return parameterJdbcTemplate.query(sql, params, new IngredientMapper());
    }
}
