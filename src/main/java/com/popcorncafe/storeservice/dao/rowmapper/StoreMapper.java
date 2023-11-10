package com.popcorncafe.storeservice.dao.rowmapper;

import com.popcorncafe.storeservice.dao.model.Address;
import com.popcorncafe.storeservice.dao.model.Store;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class StoreMapper implements RowMapper<Store> {
    @Override
    public Store mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Store(
                rs.getObject("store_id", UUID.class),
                new Address(
                        rs.getObject("address_id", UUID.class),
                        rs.getString("city_name"),
                        rs.getString("street_name"),
                        rs.getInt("home_number"),
                        rs.getString("home_letter")
                ),
                new Store.Location(
                        rs.getFloat("longitude"),
                        rs.getFloat("latitude")
                )
        );
    }
}
