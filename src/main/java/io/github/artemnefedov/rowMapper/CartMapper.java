package io.github.artemnefedov.rowMapper;

import io.github.artemnefedov.entity.Cart;
import io.github.artemnefedov.entity.Product;
import io.github.artemnefedov.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Slf4j
public class CartMapper implements RowMapper<Cart> {

    @Override
    public Cart mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Cart.builder()
                .id(resultSet.getObject("id", UUID.class))
                .clientId(resultSet.getLong("client_id"))
                .storeId(resultSet.getObject("store_id", UUID.class))
                .products(stream(((UUID[]) resultSet.getArray("products").getArray()))
                        .map(productId -> Product.builder().id(productId).build())
                        .collect(Collectors.toList()))
                .orderDate(resultSet.getTimestamp("order_date").toInstant())
                .orderPrice(resultSet.getFloat("order_price"))
                .status(Status.values()[resultSet.getInt("status")])
                .isPaid(resultSet.getBoolean("is_paid"))
                .build();
    }
}