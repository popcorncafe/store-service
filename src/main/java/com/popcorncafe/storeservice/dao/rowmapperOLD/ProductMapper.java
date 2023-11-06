//package com.popcorncafe.storeservice.dao.rowmapperOLD;
//
//import com.popcorncafe.storeservice.dao.entity.Product;
//import com.popcorncafe.storeservice.dao.entity.Size;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.UUID;
//
//public class ProductMapper implements RowMapper<Product> {
//
//    @Override
//    public Product mapRow(ResultSet resultSet, int rowNum) throws SQLException {
//        return Product.builder()
//                .id(resultSet.getObject("product_id", UUID.class))
//                .name(resultSet.getString("name"))
//                .size(Size.values()[resultSet.getInt("size")])
//                .description(resultSet.getString("description"))
//                .ingredientIdAmount(new HashMap<>())
//                .build();
//    }
//}