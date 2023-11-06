package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.AddressDao;
import com.popcorncafe.storeservice.dao.model.Address;
import com.popcorncafe.storeservice.dao.rowmapper.AddressMapper;
import com.popcorncafe.storeservice.dto.Page;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AddressDaoImpl implements AddressDao {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public AddressDaoImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    public Optional<Address> getById(UUID id) {
        var sql = """
                SELECT address_id, city_name, street_name, home_number, home_letter
                FROM address
                WHERE address_id=:address_id
                LIMIT 1;
                """;
        return parameterJdbcTemplate.query(sql, Map.of("address_id", id), new AddressMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<Address> getAll(Page page) {
        var sql = """
                SELECT address_id, city_name, street_name, home_number, home_letter
                FROM address
                LIMIT :page_size
                OFFSET :page_offset;
                """;
        var params = new MapSqlParameterSource()
                .addValue("page_size", page.size())
                .addValue("page_offset", page.offset());
        return parameterJdbcTemplate.query(sql, params, new AddressMapper());
    }

    @Override
    public UUID save(Address model) {
        var sql = """
                INSERT INTO address (city_name, street_name, home_number, home_letter)
                VALUES (:city_name, :street_name, :home_number, :home_letter)
                RETURNING address_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("city_name", model.city())
                .addValue("street_name", model.street() == null ? ' ' : model.street())
                .addValue("home_number", model.homeNumber())
                .addValue("home_letter", model.homeLetter() == null ? ' ' : model.homeLetter());
        return parameterJdbcTemplate.queryForObject(sql, params, UUID.class);
    }

    @Override
    public boolean update(Address model) {
        var sql = """
                UPDATE address
                SET city_name=:city_name,
                    street_name=:street_name,
                    home_number=:home_number,
                    home_letter=:home_letter
                WHERE address_id=:address_id;
                """;
        var params = new MapSqlParameterSource()
                .addValue("city_name", model.city())
                .addValue("street_name", model.street())
                .addValue("home_number", model.homeNumber())
                .addValue("home_letter", model.homeLetter())
                .addValue("address_id", model.addressId());
        return parameterJdbcTemplate.update(sql, params) == 1;
    }

    @Override
    public boolean delete(UUID id) {
        return parameterJdbcTemplate.update(
                "DELETE FROM address WHERE address_id=:address_id",
                Map.of("address_id", id)) == 1;
    }

    @Override
    public Optional<Address> findByStoreId(UUID id) {
        return parameterJdbcTemplate.query(
                        """
                        SELECT a.address_id, a.city_name, a.street_name, a.home_number, a.home_letter
                        FROM store s
                        LEFT JOIN address a ON s.address_id = a.address_id
                        WHERE s.store_id=:store_id
                        LIMIT 1;
                        """,
                        Map.of("store_id", id), new AddressMapper())
                .stream()
                .findFirst();
    }
}
