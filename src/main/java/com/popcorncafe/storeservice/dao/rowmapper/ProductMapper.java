package com.popcorncafe.storeservice.dao.rowmapper;

import com.popcorncafe.storeservice.dao.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Product(
                rs.getObject("product_id", UUID.class),
                rs.getString("name"),
                rs.getString("description"),
                Product.Size.valueOf(rs.getString("size")),
                new HashMap<>()
        );
    }
}
