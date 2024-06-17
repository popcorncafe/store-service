package com.popcorncafe.storeservice.repository.impl;

import com.popcorncafe.storeservice.exsception.ResourceWritingException;
import com.popcorncafe.storeservice.repository.ProductRepository;
import com.popcorncafe.storeservice.repository.model.Product;
import com.popcorncafe.storeservice.repository.rowMapper.ProductMapper;
import com.popcorncafe.storeservice.service.dto.Page;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public ProductRepositoryImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    @Cacheable(value = "ProductRepository::get", key = "#id")
    public Optional<Product> get(UUID id) {
        var sql = """
                SELECT product_id, name, description, size
                FROM product
                WHERE product_id=:product_id
                LIMIT 1;
                """;
        return parameterJdbcTemplate.query(sql, Map.of("product_id", id), new ProductMapper()).stream().findFirst();
    }

    @Override
    @Cacheable(value = "ProductRepository::getAll", key = "#page.hashCode()")
    public List<Product> getAll(Page page) {
        var sql = """
                SELECT product_id, name, description, size
                FROM product
                LIMIT :page_size
                OFFSET :page_offset;
                """;
        var params = new MapSqlParameterSource().addValue("page_size", page.size())
                .addValue("page_offset", page.offset());
        return parameterJdbcTemplate.query(sql, params, new ProductMapper());
    }

    @Override
    @Transactional
    public UUID create(Product product) {
        var sql = """
                INSERT INTO product(name, description, size)
                VALUES (:name, :description, :size)
                RETURNING product_id;
                """;
        var params = new MapSqlParameterSource().addValue("name", product.name())
                .addValue("description", product.description())
                .addValue("size", product.size().name());
        var productId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

        var ingredientSql = """
                INSERT INTO product_ingredient(product_id, ingredient_id, amount)
                VALUES (:product_id, :ingredient_id, :amount);
                """;

        int inserts = parameterJdbcTemplate.batchUpdate(ingredientSql,
                getParameterSourcesForProductIngredients(product, productId)
        ).length;

        if (inserts != product.ingredientAmount().size()) {
            throw new ResourceWritingException(
                    "Error while saving ingredients for product with id: " + productId.toString());
        }
        return productId;
    }

    @Override
    @Transactional
    @CacheEvict(value = "ProductRepository::get", key = "#product.productId()")
    public boolean update(Product product) {
        var sql = """
                UPDATE product
                SET name=:name,
                    description=:description,
                    size=:size
                WHERE product_id=:product_id;
                """;
        var params = new MapSqlParameterSource().addValue("name", product.name())
                .addValue("description", product.description())
                .addValue("size", product.size().name());
        int updates = parameterJdbcTemplate.update(sql, params);

        var ingredientSql = """
                INSERT INTO product_ingredient(product_id, ingredient_id, amount)
                VALUES (:product_id, :ingredient_id, :amount)
                ON CONFLICT (product_id, ingredient_id)
                DO UPDATE SET amount = excluded.amount;
                """;
        updates += parameterJdbcTemplate.batchUpdate(ingredientSql, getParameterSourcesForProductIngredients(product,
                product.productId()
        )).length;

        if (updates != product.ingredientAmount().size() + 1) {
            throw new ResourceWritingException(
                    "Error while updating ingredients for product with id: " + product.productId().toString());
        }
        return true;
    }

    @Override
    @CacheEvict(value = "ProductRepository::get", key = "#id")
    public boolean delete(UUID id) {
        return parameterJdbcTemplate.update(
                "DELETE FROM product WHERE product_id=:product_id;", Map.of("product_id", id)) == 1;
    }

//    @Override
//    public List<Product> getByCartId(UUID id) {
//        var sql = """
//                SELECT p.product_id, p.name, p.description, p.size
//                FROM cart_products AS cp
//                LEFT JOIN product AS p ON cp.product_id = p.product_id
//                WHERE cp.cart_id=:cart_id;
//                """;
//        return parameterJdbcTemplate.query(sql, Map.of("cart_id", id), new ProductMapper());
//    }

    private SqlParameterSource[] getParameterSourcesForProductIngredients(Product product, UUID productId) {

        Map<UUID, Float> ingredientIdAmount = product.ingredientAmount().entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(
                        entry.getKey().ingredientId(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        var parameterSources = new SqlParameterSource[ingredientIdAmount.size()];
        var ingredientIds = ingredientIdAmount.keySet().toArray(new UUID[0]);
        for (int i = 0; i < parameterSources.length; i++) {
            parameterSources[i] = new MapSqlParameterSource().addValue("product_id", productId)
                    .addValue("ingredient_id", ingredientIds[i])
                    .addValue("amount",
                            ingredientIdAmount.get(ingredientIds[i])
                    );
        }
        return parameterSources;
    }
}
