package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.ProductDao;
import com.popcorncafe.storeservice.dao.model.Product;
import com.popcorncafe.storeservice.dao.rowmapper.ProductMapper;
import com.popcorncafe.storeservice.dto.Page;
import com.popcorncafe.storeservice.exsception.ResourceWritingException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductDaoImpl implements ProductDao {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public ProductDaoImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    public Optional<Product> getById(UUID id) {
        var sql = """
                SELECT product_id, name, description, size
                FROM product
                WHERE product_id=:product_id
                LIMIT 1;
                """;
        return parameterJdbcTemplate.query(sql, Map.of("product_id", id), new ProductMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<Product> getAll(Page page) {
        var sql = """
                SELECT product_id, name, description, size
                FROM product
                LIMIT :page_size
                OFFSET :page_offset;
                """;
        var params = new MapSqlParameterSource()
                .addValue("page_size", page.size())
                .addValue("page_offset", page.offset());
        return parameterJdbcTemplate.query(sql, params, new ProductMapper());
    }

    @Override
    @Transactional
    public UUID save(Product product) {
        var sql = """
                INSERT INTO product(name, description, size)
                VALUES (:name, :description, :size)
                RETURNING product_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("name", product.name())
                .addValue("description", product.description())
                .addValue("size", product.size().name());
        var productId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

        var ingredientSql = """
                INSERT INTO product_ingredient(product_id, ingredient_id, amount)
                VALUES (:product_id, :ingredient_id, :amount);
                """;

        int inserts = parameterJdbcTemplate.batchUpdate(ingredientSql,
                getParameterSourcesForProductIngredients(product, productId)).length;

        if (inserts != product.ingredientAmount().size()) {
            throw new ResourceWritingException(
                    "Error while saving ingredients for product with id: " + productId.toString()
            );
        }
        return productId;
    }

    @Override
    @Transactional
    public boolean update(Product product) {
        var sql = """
                UPDATE product
                SET name=:name,
                    description=:description,
                    size=:size
                WHERE product_id=:product_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("name", product.name())
                .addValue("description", product.description())
                .addValue("size", product.size().name());
        int updates = parameterJdbcTemplate.update(sql, params);

        var ingredientSql = """
                INSERT INTO product_ingredient(product_id, ingredient_id, amount)
                VALUES (:product_id, :ingredient_id, :amount)
                ON CONFLICT (product_id, ingredient_id)
                DO UPDATE SET amount = excluded.amount;
                """;
        updates += parameterJdbcTemplate.batchUpdate(ingredientSql,
                getParameterSourcesForProductIngredients(product, product.productId())).length;

        if (updates != product.ingredientAmount().size() + 1) {
            throw new ResourceWritingException(
                    "Error while updating ingredients for product with id: " + product.productId().toString()
            );
        }
        return true;
    }

    @Override
    public boolean delete(UUID id) {
        return parameterJdbcTemplate.update("DELETE FROM product WHERE product_id=:product_id;", Map.of("product_id", id)) == 1;
    }

    @Override
    public List<Product> findByCartId(UUID id) {
        var sql = """
                SELECT p.product_id, p.name, p.description, p.size
                FROM cart_products AS cp
                LEFT JOIN product AS p ON cp.product_id = p.product_id
                WHERE cp.cart_id=:cart_id;
                """;
        return parameterJdbcTemplate.query(sql, Map.of("cart_id", id), new ProductMapper());
    }

    private SqlParameterSource[] getParameterSourcesForProductIngredients(Product product, UUID productId) {

        Map<UUID, Float> ingredientIdAmount =
                product.ingredientAmount().entrySet()
                        .stream()
                        .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().ingredientId(), entry.getValue()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));
        var parameterSources = new SqlParameterSource[ingredientIdAmount.size()];
        var ingredientIds = ingredientIdAmount.keySet().toArray(new UUID[0]);
        for (int i = 0; i < parameterSources.length; i++) {
            parameterSources[i] = new MapSqlParameterSource()
                    .addValue("product_id", productId)
                    .addValue("ingredient_id", ingredientIds[i])
                    .addValue("amount", ingredientIdAmount.get(ingredientIds[i]));
        }
        return parameterSources;
    }
}
