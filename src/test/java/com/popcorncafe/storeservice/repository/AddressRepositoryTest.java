package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.impl.AddressRepositoryImpl;
import com.popcorncafe.storeservice.repository.model.Address;
import com.popcorncafe.storeservice.repository.model.Store;
import com.popcorncafe.storeservice.service.dto.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class AddressRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0-alpine3.18");

    @Container
    @ServiceConnection
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2.4-alpine3.19").withExposedPorts(6379);

    private final List<Address> addresses = new ArrayList<>();
    private final Random rn = new Random();
    private Address address;

    @Autowired
    private AddressRepositoryImpl addressRepository;

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @BeforeEach
    void setUp() {

        List<Address> testAddresses = new ArrayList<>();

        for (int i = 0; i < rn.nextInt(400); i++) {
            testAddresses.add(createFakeAddress());
        }

        var sql = """
            INSERT INTO address (city_name, street_name, home_number, home_letter)
            VALUES (:city_name, :street_name, :home_number, :home_letter)
            RETURNING address_id;
            """;

        testAddresses.forEach(testAddress -> {
          var params = Map.of("city_name", testAddress.city(), "street_name", testAddress.street(), "home_number",
              testAddress.homeNumber(), "home_letter", testAddress.homeLetter()
          );

          UUID addressId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

          addresses.add(new Address(addressId, testAddress.city(), testAddress.street(), testAddress.homeNumber(),
              testAddress.homeLetter()
          ));
        });

        Address testAddress = createFakeAddress();

      var params = Map.of("city_name", testAddress.city(), "street_name", testAddress.street(), "home_number",
          testAddress.homeNumber(), "home_letter", testAddress.homeLetter()
        );

        UUID addressId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

      address = new Address(addressId, testAddress.city(), testAddress.street(), testAddress.homeNumber(),
          testAddress.homeLetter()
        );
    }

    @AfterEach
    void tearDown() {
        parameterJdbcTemplate.update("""
            DELETE FROM store;
            DELETE FROM address;
            """, new MapSqlParameterSource());
    }

    private Address createFakeAddress() {
      return new Address(null, "city #" + rn.nextInt(1000), "street #" + rn.nextInt(1000), rn.nextInt(200),
          String.valueOf(rn.nextInt(26) + 'A')
        );
    }

    @Test
    void AddressRepository_Get_ReturnsOptionalAddress() {

        var optionalAddress = addressRepository.get(address.addressId());

        assertThat(optionalAddress).isPresent();

        var expectedAddress = optionalAddress.get();

        assertThat(expectedAddress.addressId()).isEqualTo(address.addressId());
        assertThat(expectedAddress.city()).isEqualTo(address.city());
        assertThat(expectedAddress.street()).isEqualTo(address.street());
        assertThat(expectedAddress.homeNumber()).isEqualTo(address.homeNumber());
        assertThat(expectedAddress.homeLetter()).isEqualTo(address.homeLetter());
    }

    @Test
    void AddressRepository_GetAll_ReturnsListOfAddresses() {

        var actualAddresses = addressRepository.getAll(new Page(addresses.size(), 0));

        assertThat(actualAddresses).containsAll(addresses);
    }

    @Test
    void AddressRepository_Create_ReturnsAddressId() {

        Address testAddress = createFakeAddress();

        UUID addressId = addressRepository.create(testAddress);

        var optionalAddress = addressRepository.get(addressId);

        assertThat(optionalAddress).isPresent();

        var expectedAddress = optionalAddress.get();

        assertThat(expectedAddress.addressId()).isEqualTo(addressId);
        assertThat(expectedAddress.city()).isEqualTo(testAddress.city());
        assertThat(expectedAddress.street()).isEqualTo(testAddress.street());
        assertThat(expectedAddress.homeNumber()).isEqualTo(testAddress.homeNumber());
        assertThat(expectedAddress.homeLetter()).isEqualTo(testAddress.homeLetter());

        addresses.add(expectedAddress);
    }

    @Test
    void AddressRepository_Update_ReturnsBoolean() {

        Address testAddress = createFakeAddress();

        UUID addressId = addressRepository.create(testAddress);

      Address updatedAddress = new Address(addressId, "updated city", "updated street", 123, "A");

        boolean isUpdated = addressRepository.update(updatedAddress);

        assertThat(isUpdated).isTrue();

        var optionalAddress = addressRepository.get(addressId);

        assertThat(optionalAddress).isPresent();

        var expectedAddress = optionalAddress.get();

        assertThat(expectedAddress.addressId()).isEqualTo(addressId);
        assertThat(expectedAddress.city()).isEqualTo(updatedAddress.city());
        assertThat(expectedAddress.street()).isEqualTo(updatedAddress.street());
        assertThat(expectedAddress.homeNumber()).isEqualTo(updatedAddress.homeNumber());
        assertThat(expectedAddress.homeLetter()).isEqualTo(updatedAddress.homeLetter());

        addresses.removeIf(a -> a.addressId().equals(addressId));
        addresses.add(updatedAddress);
    }

    @Test
    void AddressRepository_Delete_ReturnsBoolean() {

        Address testAddress = createFakeAddress();

        UUID addressId = addressRepository.create(testAddress);

        boolean isDeleted = addressRepository.delete(addressId);

        assertThat(isDeleted).isTrue();

        var optionalAddress = addressRepository.get(addressId);

        assertThat(optionalAddress).isEmpty();

        addresses.removeIf(a -> a.addressId().equals(addressId));
    }

    @Test
    void AddressRepository_GetByStoreId_ReturnsOptionalAddress() {

      var testAddress = new Address(null, "city #" + rn.nextInt(1000), "street #" + rn.nextInt(1000), rn.nextInt(200),
          String.valueOf(rn.nextInt(26) + 'A')
        );

      var testStore = new Store(null, testAddress,
          new Store.Location(-180 + rn.nextFloat() * 360, -90 + rn.nextFloat() * 180)
        );

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

      var params = new MapSqlParameterSource().addValue("city_name", testStore.address().city())
          .addValue("street_name", testStore.address().street())
          .addValue("home_number", testStore.address().homeNumber())
          .addValue("home_letter", testStore.address().homeLetter())
          .addValue("longitude", testStore.location().longitude())
          .addValue("latitude", testStore.location().latitude());

        UUID storeId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

        var optionalAddress = addressRepository.getByStoreId(storeId);

        assertThat(optionalAddress).isPresent();

        var expectedAddress = optionalAddress.get();

        assertThat(expectedAddress.city()).isEqualTo(testStore.address().city());
        assertThat(expectedAddress.street()).isEqualTo(testStore.address().street());
        assertThat(expectedAddress.homeNumber()).isEqualTo(testStore.address().homeNumber());
        assertThat(expectedAddress.homeLetter()).isEqualTo(testStore.address().homeLetter());

        addresses.add(expectedAddress);
    }
}