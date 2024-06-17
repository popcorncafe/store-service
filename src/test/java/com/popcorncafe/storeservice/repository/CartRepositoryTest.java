package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.impl.CartRepositoryImpl;
import com.popcorncafe.storeservice.repository.model.Cart;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
@Testcontainers
class CartRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:16.0-alpine3.18");

    @Container
    @ServiceConnection
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2.4-alpine3.19").withExposedPorts(6379);

    private final List<Cart> carts = new ArrayList<>();
    private final Random rn = new Random();
    private Cart cart;
    @Autowired
    private CartRepositoryImpl cartRepository;

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        List<Cart> testCarts = new ArrayList<>();

        for (int i = 0; i < rn.nextInt(400); i++) {
            testCarts.add(createFakeCart());
        }

        var sql = """
                INSERT INTO cart (client_id, store_id, order_date, order_price, status)
                VALUES (:client_id, :store_id, :order_date, :order_price, :status)
                RETURNING cart_id;
                """;

        testCarts.forEach(cart -> {
            var params = Map.of("client_id", cart.clientId(), "storeId", cart.storeId(), "order_date", cart.orderDate(),
                    "order_price", cart.orderPrice(), "status", cart.status().name()
            );

            UUID cartId = parameterJdbcTemplate.queryForObject(sql, params, UUID.class);

            carts.add(new Cart(cartId, cart.clientId(), cart.storeId(), cart.orderDate(), cart.orderPrice(),
                    cart.status(), cart.products()
            ));
        });

        carts.addAll(testCarts);
    }

    private Cart createFakeCart() {
        return new Cart(null, rn.nextInt(100), UUID.randomUUID(), Instant.now(), rn.nextInt(100),
                Cart.Status.values()[rn.nextInt(Cart.Status.values().length)], new ArrayList<>()
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void get() {
    }

    @Test
    void getAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getByClient() {
    }

    @Test
    void getByStore() {
    }
}