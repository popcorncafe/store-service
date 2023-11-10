package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.StoreDao;
import com.popcorncafe.storeservice.dao.model.Store;
import com.popcorncafe.storeservice.dao.rowmapper.StoreMapper;
import com.popcorncafe.storeservice.dto.Page;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StoreDaoImpl implements StoreDao {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public StoreDaoImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    public Optional<Store> getById(UUID id) {

        var sql = """
                SELECT s.store_id, s.location[0] AS longitude, s.location[1] AS latitude, a.city_name, a.street_name, a.home_number, a.home_letter
                FROM store AS s
                INNER JOIN address AS a ON s.address_id = a.address_id
                WHERE s.store_id=:store_id
                LIMIT 1;
                """;
        return parameterJdbcTemplate.query(sql, Map.of("store_id", id), new StoreMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<Store> getAll(Page page) {
        var sql = """
                SELECT s.store_id, s.location[0] AS longitude, s.location[1] AS latitude, a.city_name, a.street_name, a.home_number, a.home_letter
                FROM store AS s
                INNER JOIN address AS a ON s.address_id = a.address_id
                LIMIT :page_size
                OFFSET :page_offset;
                """;
        var params = new MapSqlParameterSource()
                .addValue("page_size", page.size())
                .addValue("page_offset", page.offset());
        return parameterJdbcTemplate.query(sql, params, new StoreMapper());
    }

    @Override
    public UUID save(Store store) {
        var sql = """
                WITH new_address AS (
                    INSERT INTO address (city_name, street_name, home_number, home_letter)
                    VALUES (:city_name, :street_name, :home_number, :home_letter)
                    RETURNING address_id
                )
                INSERT INTO store(address_id, location)
                VALUES ((SELECT address_id FROM new_address), point(:longitude, :latitude))
                RETURNING store_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("city_name", store.address().city())
                .addValue("street_name", store.address().street())
                .addValue("home_number", store.address().homeNumber())
                .addValue("home_letter", store.address().homeLetter())
                .addValue("longitude", store.location().longitude())
                .addValue("latitude", store.location().latitude());
        return parameterJdbcTemplate.queryForObject(sql, params, UUID.class);
    }

    @Override
    public boolean update(Store store) {
        var sql = """
                UPDATE store
                SET location=point(:longitude, :latitude)
                WHERE store_id=:store_id;
                                
                UPDATE address
                SET city_name=:city_name,
                    street_name=:street_name,
                    home_number=:home_number,
                    home_letter=:home_letter
                WHERE address_id=:address_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("longitude", store.location().longitude())
                .addValue("latitude", store.location().latitude())
                .addValue("store_id", store.storeId())
                .addValue("city_name", store.address().city())
                .addValue("street_name", store.address().street())
                .addValue("home_number", store.address().homeNumber())
                .addValue("home_letter", store.address().homeLetter())
                .addValue("address_id", store.address().addressId());
        return parameterJdbcTemplate.update(sql, params) == 1;
    }

    @Override
    public boolean delete(UUID id) {
        return parameterJdbcTemplate.update("DELETE FROM store WHERE store_id=:store_id;", Map.of("store_id", id)) == 1;
    }
}