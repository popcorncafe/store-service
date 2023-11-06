//package com.popcorncafe.storeservice.dao.rowmapperOLD;
//
//import com.popcorncafe.storeservice.dao.entity.Ingredient;
//import com.popcorncafe.storeservice.dao.entity.Measure;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.UUID;
//
//public class IngredientMapper implements RowMapper<Ingredient> {
//
//    @Override
//    public Ingredient mapRow(ResultSet resultSet, int rowNum) throws SQLException {
//        return Ingredient.builder()
//                .id(resultSet.getObject("id", UUID.class))
//                .name(resultSet.getString("name"))
//                .unitPrice(resultSet.getFloat("unit_price"))
//                .measure(Measure.values()[resultSet.getInt("measure")])
//                .build();
//    }
//}
