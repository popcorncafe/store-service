package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.impl.CartRepositoryImpl;
import com.popcorncafe.storeservice.repository.model.Cart;
import com.popcorncafe.storeservice.service.dto.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class CartRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>(
            "postgres:16.0-alpine3.18");

    @Container
    @ServiceConnection
    static GenericContainer<?> redisContainer = new GenericContainer<>(
            "redis:7.2.4-alpine3.19").withExposedPorts(6379);

    private final List<Cart> carts = new ArrayList<>();
    private final Random rn = new Random();
    private UUID storeId;
    @Autowired
    private CartRepositoryImpl cartRepository;

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        List<Cart> testCarts = new ArrayList<>();

        storeId = UUID.randomUUID();

        for (int i = 0; i < rn.nextInt(400); i++) {
            testCarts.add(createFakeCart());
        }

        parameterJdbcTemplate.update(
                """
                        WITH new_address AS (
                            INSERT INTO address (city_name, street_name, home_number, home_letter)
                            VALUES ('city', 'street', 1, 'a')
                            RETURNING address_id
                        )
                        INSERT INTO store (store_id, address_id, location)
                        VALUES (:store_id, (SELECT address_id FROM new_address), point(1, 1));
                        """,
                Map.of("store_id", storeId)
        );

        var sql = """
                INSERT INTO cart (client_id, store_id, order_date, order_price, status)
                VALUES (:client_id, :store_id, :order_date, :order_price, :status)
                RETURNING cart_id;
                """;

        testCarts.forEach(cart -> {
            var params = Map.of(
                    "client_id", cart.clientId(),
                    "store_id", cart.storeId(),
                    "order_date", Timestamp.from(cart.orderDate()),
                    "order_price", cart.orderPrice(),
                    "status", cart.status().name()
            );

            UUID cartId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

            carts.add(new Cart(cartId, cart.clientId(), cart.storeId(), cart.orderDate(),
                               cart.orderPrice(),
                               cart.status(), cart.products()
            ));
        });
    }

    @AfterEach
    void tearDown() {
        parameterJdbcTemplate.update(
                """
                        DELETE FROM cart;
                        DELETE FROM store;
                        DELETE FROM address;
                        """,
                Map.of()
        );
    }

    private Cart createFakeCart() {
        return new Cart(null, rn.nextInt(10000) + 1, storeId, Instant.now(), rn.nextInt(100),
                        Cart.Status.values()[rn.nextInt(Cart.Status.values().length)], new ArrayList<>()
        );
    }

    @Test
    void CartRepository_Get_ReturnsOptionalCart() {
        Cart cart = carts.get(rn.nextInt(carts.size()));

        var optionalCart = cartRepository.get(cart.cartId());

        assertThat(optionalCart).isPresent();
        Cart expectedCart = optionalCart.get();

        assertThat(expectedCart).usingRecursiveComparison().ignoringFields("orderDate").isEqualTo(cart);
    }

    @Test
    void CartRepository_GetAll_ReturnsListOfCarts() {
        var actualCarts = cartRepository.getAll(new Page(carts.size(), 0));

        assertThat(actualCarts).hasSize(carts.size());
        assertThat(actualCarts).usingRecursiveComparison().ignoringFields("orderDate").isEqualTo(carts);
    }

    @Test
    void CartRepository_Create_ReturnsNewCartId() {

        Cart cart = createFakeCart();

        UUID cartId = cartRepository.create(cart);

        assertThat(cartId).isNotNull();

        Cart actualCart = new Cart(cartId, cart.clientId(), cart.storeId(), cart.orderDate(),
                                   cart.orderPrice(), cart.status(), cart.products()
        );

        var optionalCart = cartRepository.get(actualCart.cartId());

        assertThat(optionalCart).isPresent();

        Cart expectedCart = optionalCart.get();

        assertThat(expectedCart).usingRecursiveComparison().ignoringFields("orderDate").isEqualTo(actualCart);
    }

    @Test
    void CartRepository_Update_ReturnsResult() {

        Cart cart = carts.get(rn.nextInt(carts.size()));

        Cart updatedCart = new Cart(cart.cartId(), cart.clientId(), cart.storeId(), cart.orderDate(),
                                    cart.orderPrice(), Cart.Status.COMPLETE, cart.products()
        );

        boolean result = cartRepository.update(updatedCart);

        assertThat(result).isTrue();

        var optionalCart = cartRepository.get(updatedCart.cartId());

        assertThat(optionalCart).isPresent();

        Cart expectedCart = optionalCart.get();

        assertThat(expectedCart).usingRecursiveComparison().ignoringFields("orderDate").isEqualTo(updatedCart);
    }

    @Test
    void CartRepository_Delete_ReturnsResult() {
        Cart cart = carts.get(rn.nextInt(carts.size()));

        boolean result = cartRepository.delete(cart.cartId());

        assertThat(result).isTrue();

        var optionalCart = cartRepository.get(cart.cartId());

        assertThat(optionalCart).isEmpty();
    }

    @Test
    void CartRepository_GetByClient_ReturnsListOfCarts() {

        List<Cart> clientCarts = new ArrayList<>();

        long clientId = rn.nextInt(10000) + 1;

        for (int i = 0; i < rn.nextInt(20) + 10; i++) {
            clientCarts.add(
                    new Cart(
                            null,
                            clientId,
                            storeId,
                            Instant.now(),
                            rn.nextInt(100),
                            Cart.Status.values()[rn.nextInt(Cart.Status.values().length)],
                            new ArrayList<>()
                    )
            );
        }

        var sql = """
                INSERT INTO cart (client_id, store_id, order_date, order_price, status)
                VALUES (:client_id, :store_id, :order_date, :order_price, :status)
                RETURNING cart_id;
                """;

        clientCarts.forEach(cart -> {
            var params = Map.of(
                    "client_id", cart.clientId(),
                    "store_id", cart.storeId(),
                    "order_date", Timestamp.from(cart.orderDate()),
                    "order_price", cart.orderPrice(),
                    "status", cart.status().name()
            );

            UUID cartId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

            carts.add(new Cart(cartId, cart.clientId(), cart.storeId(), cart.orderDate(),
                               cart.orderPrice(),
                               cart.status(), cart.products()
            ));
        });

        var actualCarts = cartRepository.getByClient(clientId);

        assertThat(actualCarts).hasSize(clientCarts.size());
        assertThat(actualCarts).usingRecursiveComparison().ignoringFields("orderDate", "cartId").isEqualTo(clientCarts);
    }

    @Test
    void CartRepository_GetByStore_ReturnsListOfCarts() {

        List<Cart> storeCarts = new ArrayList<>();

        UUID testStoreId = UUID.randomUUID();

        parameterJdbcTemplate.update(
                """
                        WITH new_address AS (
                            INSERT INTO address (city_name, street_name, home_number, home_letter)
                            VALUES ('city', 'street', 1, 'a')
                            RETURNING address_id
                        )
                        INSERT INTO store (store_id, address_id, location)
                        VALUES (:store_id, (SELECT address_id FROM new_address), point(1, 1));
                        """,
                Map.of("store_id", testStoreId)
        );

        for (int i = 0; i < rn.nextInt(20) + 10; i++) {
            storeCarts.add(
                    new Cart(
                            null,
                            rn.nextInt(10000) + 1,
                            testStoreId,
                            Instant.now(),
                            rn.nextInt(100),
                            Cart.Status.values()[rn.nextInt(Cart.Status.values().length)],
                            new ArrayList<>()
                    )
            );
        }

        var sql = """
                INSERT INTO cart (client_id, store_id, order_date, order_price, status)
                VALUES (:client_id, :store_id, :order_date, :order_price, :status)
                RETURNING cart_id;
                """;

        storeCarts.forEach(cart -> {
            var params = Map.of(
                    "client_id", cart.clientId(),
                    "store_id", cart.storeId(),
                    "order_date", Timestamp.from(cart.orderDate()),
                    "order_price", cart.orderPrice(),
                    "status", cart.status().name()
            );

            UUID cartId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

            carts.add(new Cart(cartId, cart.clientId(), cart.storeId(), cart.orderDate(),
                               cart.orderPrice(),
                               cart.status(), cart.products()
            ));
        });

        var actualCarts = cartRepository.getByStore(testStoreId, new Page(storeCarts.size(), 0));

        assertThat(actualCarts).hasSize(storeCarts.size());
        assertThat(actualCarts).usingRecursiveComparison().ignoringFields("orderDate", "cartId").isEqualTo(storeCarts);
    }
}
