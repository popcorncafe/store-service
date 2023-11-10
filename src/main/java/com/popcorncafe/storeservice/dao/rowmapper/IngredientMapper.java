package com.popcorncafe.storeservice.dao.rowmapper;

import com.popcorncafe.storeservice.dao.model.Ingredient;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class IngredientMapper implements RowMapper<Ingredient> {
    @Override
    public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Ingredient(
                rs.getObject("ingredient_id", UUID.class),
                rs.getString("name"),
                rs.getFloat("unit_price"),
                Ingredient.Measure.valueOf(rs.getString("measure"))
        );
    }
}
