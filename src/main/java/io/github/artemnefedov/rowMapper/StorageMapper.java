package io.github.artemnefedov.rowMapper;

import io.github.artemnefedov.entity.Storage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class StorageMapper implements RowMapper<Storage> {

    @Override
    public Storage mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        var storage = Storage.builder()
                .id(resultSet.getObject("id", UUID.class))
                .ingredientIDAmount(new HashMap<>())
                .build();
        do {
            storage.getIngredientIDAmount().put(
                    resultSet.getObject("ingredient_id", UUID.class),
                    resultSet.getFloat("amount")
            );
        } while (resultSet.next());
        return storage;
    }
}
