//package com.popcorncafe.storeservice.dao.rowmapperOLD;
//
//import com.popcorncafe.storeservice.dao.entity.Address;
//import com.popcorncafe.storeservice.dto.Location;
//import com.popcorncafe.storeservice.dao.entity.Store;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.UUID;
//
//public class StoreMapper implements RowMapper<Store> {
//
//    @Override
//    public Store mapRow(ResultSet resultSet, int rowNum) throws SQLException {
//        return Store.builder()
//                .id(resultSet.getObject("id", UUID.class))
//                .address(
//                        new Address(
//                                resultSet.getObject("address_id", UUID.class),
//                                resultSet.getString("city_name"),
//                                resultSet.getString("street_name"),
//                                resultSet.getInt("home_number"),
//                                resultSet.getString("home_letter")
//                        )
//                )
//                .location(new Location(
//                        resultSet.getFloat("longitude"),
//                        resultSet.getFloat("latitude")
//                ))
//                .build();
//    }
//}
