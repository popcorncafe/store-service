package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.CartDao;
import com.popcorncafe.storeservice.dao.ProductDao;
import com.popcorncafe.storeservice.dao.model.Cart;
import com.popcorncafe.storeservice.dao.model.Product;
import com.popcorncafe.storeservice.dao.rowmapper.AddressMapper;
import com.popcorncafe.storeservice.dao.rowmapper.CartMapper;
import com.popcorncafe.storeservice.dto.Page;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return parameterJdbcTemplate.query(sql, Map.of("cart_id", id), new CartMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<Cart> getAll(Page page) {
        var sql = """
                SELECT cart_id, client_id, store_id, order_date, order_price, status
                FROM cart
                LIMIT :page_size
                OFFSET :page_offset;
                """;
        var params = new MapSqlParameterSource()
                .addValue("page_size", page.size())
                .addValue("page_offset", page.offset());
        return parameterJdbcTemplate.query(sql, params, new CartMapper());
    }

    @Override
    public UUID save(Cart model) {
        return null;
    }

    @Override
    public boolean update(Cart model) {
        return false;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public Optional<Cart> findByStoreId(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Cart> findByClientId(long id) {
        return null;
    }
}
