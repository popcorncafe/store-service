package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.CartDao;
import com.popcorncafe.storeservice.dao.model.Cart;
import com.popcorncafe.storeservice.dao.rowmapper.CartMapper;
import com.popcorncafe.storeservice.dto.Page;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CartDaoImpl implements CartDao {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public CartDaoImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    public Optional<Cart> getById(UUID id) {
        var sql = """
                SELECT cart_id, client_id, store_id, order_date, order_price, status
                FROM cart
                WHERE cart_id=:cart_id
                LIMIT 1;
                """;
        return parameterJdbcTemplate.query(sql, Map.of("cart_id", id), new CartMapper()).stream().findFirst();
    }

    @Override
    public List<Cart> getAll(Page page) {
        var sql = """
                SELECT cart_id, client_id, store_id, order_date, order_price, status
                FROM cart
                LIMIT :page_size
                OFFSET :page_offset;
                """;
        var params = new MapSqlParameterSource().addValue("page_size", page.size()).addValue("page_offset", page.offset());
        return parameterJdbcTemplate.query(sql, params, new CartMapper());
    }

    @Override
    public UUID save(Cart cart) {
        var sql = """
                INSERT INTO cart (client_id, store_id, order_price, status)
                VALUES (:client_id, :store_id, :order_price, :status)
                RETURNING cart_id;
                """;

        var params = new MapSqlParameterSource()
                .addValue("client_id", cart.clientId())
                .addValue("storeId", cart.storeId())
                .addValue("order_price", cart.orderPrice())
                .addValue("status", cart.status().name());

        return parameterJdbcTemplate.queryForObject(sql, params, UUID.class);
    }

    @Override
    public boolean update(Cart cart) {
        var sql = """
                UPDATE cart
                SET status=:status
                WHERE cart_id=:cart_id
                """;

        var params = new MapSqlParameterSource()
                .addValue("storeId", cart.storeId())
                .addValue("status", cart.status().name());

        return parameterJdbcTemplate.update(sql, params) == 1;
    }

    @Override
    public boolean delete(UUID id) {
        return parameterJdbcTemplate.update(
                "DELETE FROM cart WHERE cart_id=:cart_id;",
                Map.of("cart_id", id)) == 1;
    }

    @Override
    public List<Cart> findByClientId(long id) {
        var sql = """
                SELECT cart_id, client_id, store_id, order_date, order_price, status
                FROM cart
                WHERE client_id=:client_id
                LIMIT 50;
                """;
        return parameterJdbcTemplate.query(sql, Map.of("client_id", id), new CartMapper());
    }

    @Override
    public List<Cart> findByStoreId(UUID id, Page page) {
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
        return parameterJdbcTemplate.query(sql, params, new CartMapper());
    }
}
