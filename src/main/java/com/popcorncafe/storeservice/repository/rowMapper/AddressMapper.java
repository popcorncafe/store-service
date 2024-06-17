package com.popcorncafe.storeservice.repository.rowMapper;

import com.popcorncafe.storeservice.repository.model.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AddressMapper implements RowMapper<Address> {

    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Address(rs.getObject("address_id", UUID.class), rs.getString("city_name"),
                rs.getString("street_name"), rs.getInt("home_number"), rs.getString("home_letter")
        );
    }
}
