package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.impl.StoreRepositoryImpl;
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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class StoreRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:16.0-alpine3.18");

    @Container
    @ServiceConnection
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2.4-alpine3.19").withExposedPorts(6379);

    private final List<Store> stores = new ArrayList<>();
    private final Random rn = new Random();
    private Store store;
    @Autowired
    private StoreRepositoryImpl storeRepository;

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @BeforeEach
    void setUp() {

        List<Store> testStores = new ArrayList<>();

        for (int i = 0; i < rn.nextInt(400); i++) {
            testStores.add(createFakeStore());
        }

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

        testStores.forEach(store -> {

            var params = new MapSqlParameterSource().addValue("city_name", store.address().city())
                    .addValue("street_name", store.address().street())
                    .addValue("home_number", store.address().homeNumber())
                    .addValue("home_letter", store.address().homeLetter())
                    .addValue("longitude", store.location().longitude())
                    .addValue("latitude", store.location().latitude());

            UUID storeId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

            stores.add(new Store(storeId, store.address(), store.location()));
        });

        Store testStore = createFakeStore();

        var params = new MapSqlParameterSource().addValue("city_name", testStore.address().city())
                .addValue("street_name", testStore.address().street())
                .addValue("home_number", testStore.address().homeNumber())
                .addValue("home_letter", testStore.address().homeLetter())
                .addValue("longitude", testStore.location().longitude())
                .addValue("latitude", testStore.location().latitude());

        UUID storeId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

        store = new Store(storeId, testStore.address(), testStore.location());
    }

    @AfterEach
    void tearDown() {
        parameterJdbcTemplate.update("""
                DELETE FROM store;
                DELETE FROM address;
                """, new MapSqlParameterSource());
    }

    private Store createFakeStore() {
        return new Store(null, new Address(null, "city #" + rn.nextInt(10000), "street #" + rn.nextInt(10000),
                rn.nextInt(100), String.valueOf((char) (rn.nextInt(26) + 'a'))
        ), new Store.Location(-180 + rn.nextFloat() * 360, -90 + rn.nextFloat() * 180));
    }

    @Test
    void StoreRepository_Get_ReturnsOptionalStore() {
        var optionalStore = storeRepository.get(store.storeId());

        assertThat(optionalStore).isPresent();

        var expectedStore = optionalStore.get();

        assertThat(expectedStore.storeId()).isEqualTo(store.storeId());
        assertThat(expectedStore.address().city()).isEqualTo(store.address().city());
        assertThat(expectedStore.address().street()).isEqualTo(store.address().street());
        assertThat(expectedStore.address().homeNumber()).isEqualTo(store.address().homeNumber());
        assertThat(expectedStore.address().homeLetter()).isEqualTo(store.address().homeLetter());
        assertThat(expectedStore.location()).isEqualTo(store.location());
    }

    @Test
    void StoreRepository_GetAll_ReturnsListOfStores() {
        var expectedStores = storeRepository.getAll(new Page(stores.size(), 0));

        assertThat(expectedStores.size()).isEqualTo(stores.size());

        expectedStores.forEach(expectedStore -> {
            var store = stores.stream().filter(s -> s.storeId().equals(expectedStore.storeId())).findFirst()
                    .orElseThrow();

            assertThat(expectedStore.storeId()).isEqualTo(store.storeId());
            assertThat(expectedStore.address().city()).isEqualTo(store.address().city());
            assertThat(expectedStore.address().street()).isEqualTo(store.address().street());
            assertThat(expectedStore.address().homeNumber()).isEqualTo(store.address().homeNumber());
            assertThat(expectedStore.address().homeLetter()).isEqualTo(store.address().homeLetter());
            assertThat(expectedStore.location()).isEqualTo(store.location());
        });
    }

    @Test
    void StoreRepository_Create_ReturnsStoreId() {
        var testStore = createFakeStore();

        var storeId = storeRepository.create(testStore);

        var optionalStore = storeRepository.get(storeId);

        assertThat(optionalStore).isPresent();

        var expectedStore = optionalStore.get();

        assertThat(expectedStore.storeId()).isEqualTo(storeId);
        assertThat(expectedStore.address().city()).isEqualTo(testStore.address().city());
        assertThat(expectedStore.address().street()).isEqualTo(testStore.address().street());
        assertThat(expectedStore.address().homeNumber()).isEqualTo(testStore.address().homeNumber());
        assertThat(expectedStore.address().homeLetter()).isEqualTo(testStore.address().homeLetter());
        assertThat(expectedStore.location()).isEqualTo(testStore.location());

        stores.add(expectedStore);
    }

    @Test
    void StoreRepository_Update_ReturnsBoolean() {
        var testStore = createFakeStore();

        var storeId = storeRepository.create(testStore);

        var updatedStore = new Store(storeId,
                new Address(null, "city #" + rn.nextInt(10000), "street #" + rn.nextInt(10000),
                        rn.nextInt(100), String.valueOf((char) (rn.nextInt(26) + 'a'))
                ), new Store.Location(-180 + rn.nextFloat() * 360, -90 + rn.nextFloat() * 180)
        );

        var updated = storeRepository.update(updatedStore);

        assertThat(updated).isTrue();

        var optionalStore = storeRepository.get(storeId);

        assertThat(optionalStore).isPresent();

        var expectedStore = optionalStore.get();

        assertThat(expectedStore.storeId()).isEqualTo(storeId);
        assertThat(expectedStore.address().street()).isEqualTo(updatedStore.address().street());
        assertThat(expectedStore.address().homeNumber()).isEqualTo(updatedStore.address().homeNumber());
        assertThat(expectedStore.address().homeLetter()).isEqualTo(updatedStore.address().homeLetter());
        assertThat(expectedStore.location()).isEqualTo(updatedStore.location());
        assertThat(expectedStore.address().city()).isEqualTo(updatedStore.address().city());

        stores.removeIf(s -> s.storeId().equals(storeId));
        stores.add(expectedStore);
    }

    @Test
    void StoreRepository_Delete_ReturnsBoolean() {
        var testStore = createFakeStore();

        var storeId = storeRepository.create(testStore);

        var deleted = storeRepository.delete(storeId);

        assertThat(deleted).isTrue();

        var optionalStore = storeRepository.get(storeId);

        assertThat(optionalStore).isEmpty();

        stores.removeIf(s -> s.storeId().equals(storeId));
    }

    @Test
    void StoreRepository_GetByLocation_ReturnsListOfNearbyStoresMaxTo3() {
        var testStoresId = new ArrayList<UUID>();

        var testLocation = new Store.Location(-180 + rn.nextFloat() * 360, -90 + rn.nextFloat() * 180);

        for (int i = 0; i < 3; i++) {

            var testStore = new Store(null,
                    new Address(null, "city #" + rn.nextInt(10000), "street #" + rn.nextInt(10000),
                            rn.nextInt(100), String.valueOf((char) (rn.nextInt(26) + 'a'))
                    ), testLocation
            );

            testStoresId.add(storeRepository.create(testStore));
        }

        var testStores = testStoresId.stream().map(storeRepository::get).map(Optional::orElseThrow).toList();

        var expectedStores = storeRepository.getByLocation(testLocation);

        assertThat(expectedStores.size()).isLessThanOrEqualTo(3);
        assertThat(expectedStores).containsAll(testStores);
    }
}