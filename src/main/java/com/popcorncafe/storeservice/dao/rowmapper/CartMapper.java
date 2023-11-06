package com.popcorncafe.storeservice.dao.rowmapper;

import com.popcorncafe.storeservice.dao.model.Cart;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class CartMapper implements RowMapper<Cart> {
    @Override
    public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Cart(
                rs.getObject("cart_id", UUID.class),
                rs.getLong("client_id"),
                rs.getObject("store_id", UUID.class),
                rs.getObject("order_date", Instant.class),
                rs.getLong("order_price"),
                Cart.Status.valueOf(rs.getString("status")),
                new ArrayList<>()
        );
    }
}
