package com.popcorncafe.storeservice.dao.rowmapper;

import com.popcorncafe.storeservice.dao.model.Store;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreMapper implements RowMapper<Store> {
    @Override
    public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
