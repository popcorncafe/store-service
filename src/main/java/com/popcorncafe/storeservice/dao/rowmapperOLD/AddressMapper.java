//package com.popcorncafe.storeservice.dao.rowmapperOLD;
//
//import com.popcorncafe.storeservice.dao.entity.Address;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.UUID;
//
//public class AddressMapper implements RowMapper<Address> {
//
//    @Override
//    public Address mapRow(ResultSet resultSet, int rowNum) throws SQLException {
//        return new Address(
//                resultSet.getObject("address_id", UUID.class),
//                resultSet.getString("city_name"),
//                resultSet.getString("street_name"),
//                resultSet.getInt("home_number"),
//                resultSet.getString("home_letter")
//        );
//    }
//}
