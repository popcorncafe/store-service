package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.StoreDao;
import com.popcorncafe.storeservice.dao.model.Store;
import com.popcorncafe.storeservice.dto.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StoreDaoImpl implements StoreDao {


    @Override
    public Optional<Store> getById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Store> getAll(Page page) {
        return null;
    }

    @Override
    public UUID save(Store model) {
        return null;
    }

    @Override
    public boolean update(Store model) {
        return false;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }
}
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Store> getAllStores() {
//        var sql = """
//                SELECT st.id,
//                       st.location[0] as longitude,
//                       st.location[1] as latitude,
//                       a.id           AS address_id,
//                       a.city_name,
//                       a.street_name,
//                       a.home_number,
//                       a.home_letter
//                FROM store AS st
//                         JOIN address a on a.id = st.address_id;
//                """;
//        return jdbcTemplate.query(sql, new StoreMapper())
//                .stream()
//                .peek(store -> store.setStorage(getStorageByStoreId(store.getId()).orElse(new Storage())))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    @Cacheable(value = "StoreRepository::getStoreById", key = "#id")
//    public Optional<Store> getStoreById(UUID id) {
//        var sql = """
//                SELECT st.id,
//                       st.location[0] as longitude,
//                       st.location[1] as latitude,
//                       a.id           AS address_id,
//                       a.city_name,
//                       a.street_name,
//                       a.home_number,
//                       a.home_letter
//                FROM store AS st
//                         JOIN address a on a.id = st.address_id
//                WHERE st.id = ?
//                LIMIT 1;
//                """;
//        return jdbcTemplate.query(sql, new StoreMapper(), id)
//                .stream()
//                .peek(store -> store.setStorage(getStorageByStoreId(store.getId()).orElse(new Storage())))
//                .findFirst();
//    }
//
//    private Optional<Storage> getStorageByStoreId(UUID id) {
//        var sql = """
//                SELECT st.id, st_i.ingredient_id, st_i.amount
//                FROM store AS s
//                         JOIN storage AS st ON s.storage_id = st.id
//                         JOIN storage_ingredient AS st_i ON st_i.storage_id = st.id
//                         JOIN ingredient AS i ON i.id = st_i.ingredient_id
//                WHERE s.id = ?;
//                """;
//        return jdbcTemplate.query(sql, new StorageMapper(), id)
//                .stream()
//                .findFirst();
//    }
//
//    @Override
//    @Transactional
//    public UUID addStore(Store store) {
//        var sql = """
//                WITH new_address AS (
//                    INSERT INTO address (city_name, street_name, home_number, home_letter)
//                        VALUES (?, ?, ?, ?)
//                        RETURNING id),
//                    new_storage AS (
//                        INSERT INTO storage (id)
//                            VALUES (default)
//                                RETURNING id)
//
//                INSERT INTO store(address_id, storage_id, location)
//                VALUES ((SELECT id FROM new_address), (SELECT id FROM new_storage), point(?, ?))
//                RETURNING store.id;
//                """;
//        var storeId = jdbcTemplate.queryForObject(sql,
//                UUID.class,
//                store.getAddress().cityName(),
//                store.getAddress().streetName(),
//                store.getAddress().homeNumber(),
//                store.getAddress().homeLetter(),
//                store.getLocation().longitude(),
//                store.getLocation().latitude());
//        var newStorageId = jdbcTemplate.queryForObject(
//                """
//                        SELECT s.storage_id
//                        FROM store AS s
//                        WHERE s.id = ?;
//                        """,
//                UUID.class,
//                storeId);
//        var storageSql = """
//                INSERT INTO storage_ingredient (storage_id, ingredient_id, amount)
//                VALUES (?, ?, ?)
//                """;
//        var ingredientKeys = store.getStorage().getIngredientIDAmount().keySet();
//        jdbcTemplate.batchUpdate(
//                storageSql,
//                ingredientKeys,
//                ingredientKeys.size(),
//                (PreparedStatement prepare, UUID ingredientId) -> {
//                    prepare.setObject(1, newStorageId);
//                    prepare.setObject(2, ingredientId);
//                    prepare.setFloat(3, store.getStorage().getIngredientIDAmount().get(ingredientId));
//                });
//
//        return storeId;
//    }
//
//    @Override
//    @Transactional
//    public boolean updateStore(Store store) {
//        var sql = """
//                UPDATE store
//                SET address_id = ?,
//                    storage_id = ?,
//                    location = point(?, ?)
//                WHERE id = ?
//                """;
//        return jdbcTemplate.update(sql,
//                store.getAddress().id(),
//                store.getStorage().getId(),
//                store.getLocation().longitude(),
//                store.getLocation().latitude(),
//                store.getId()) >= 1 && updateStorage(store.getStorage());
//    }
//
//    @Override
//    @Transactional
//    public boolean updateStorage(Storage storage) {
//        var newIngredientIDAmount = storage.getIngredientIDAmount();
//        var originalIngredientIDAmount = jdbcTemplate.query(
//                """
//                        SELECT st_i.ingredient_id, st_i.amount
//                        FROM storage_ingredient AS st_i
//                        WHERE st_i.storage_id = ?;
//                        """,
//                resultSet -> {
//                    Map<UUID, Float> ingredientIDAmount = new HashMap<>();
//                    while (resultSet.next()) {
//                        ingredientIDAmount.put(
//                                resultSet.getObject("ingredient_id", UUID.class),
//                                resultSet.getFloat("amount")
//                        );
//                    }
//                    return ingredientIDAmount;
//                },
//                storage.getId());
//        List<UUID> updateIngredients = new ArrayList<>();
//        List<UUID> insertIngredients = new ArrayList<>();
//
//        newIngredientIDAmount.keySet().forEach(ingredientId -> {
//            if (originalIngredientIDAmount.containsKey(ingredientId)) {
//                if (!originalIngredientIDAmount.get(ingredientId).equals(newIngredientIDAmount.get(ingredientId))) {
//                    updateIngredients.add(ingredientId);
//                }
//            } else {
//                insertIngredients.add(ingredientId);
//            }
//        });
//        jdbcTemplate.batchUpdate("""
//                        UPDATE storage_ingredient
//                        SET amount = ?
//                        WHERE ingredient_id = ? AND storage_id = ?;
//                        """,
//                updateIngredients, updateIngredients.size(),
//                (PreparedStatement prepare, UUID ingredientId) -> {
//                    prepare.setFloat(1, newIngredientIDAmount.get(ingredientId));
//                    prepare.setObject(2, ingredientId);
//                    prepare.setObject(3, storage.getId());
//                });
//
//        jdbcTemplate.batchUpdate("""
//                        INSERT INTO storage_ingredient (storage_id, ingredient_id, amount)
//                        VALUES (?, ?, ?)
//                        """,
//                insertIngredients, insertIngredients.size(),
//                (PreparedStatement prepare, UUID ingredientId) -> {
//                    prepare.setObject(1, storage.getId());
//                    prepare.setObject(2, ingredientId);
//                    prepare.setFloat(3, newIngredientIDAmount.get(ingredientId));
//                });
//        return true;
//    }
//
//    @Override
//    @Transactional
//    @CacheEvict(value = "StoreService::getStoreById", key = "#id")
//    public boolean deleteStore(UUID id) {
//        return jdbcTemplate.update("DELETE FROM store WHERE id = ?", id) >= 1;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    @Cacheable("StoreRepository::getAllIngredients")
//    public List<Ingredient> getAllIngredients() {
//        return jdbcTemplate.query("SELECT * FROM ingredient", new IngredientMapper());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Store> getStoresByLocation(Location location) {
//        var sql = """
//                SELECT st.id,
//                       st.location[0] as longitude,
//                       st.location[1] as latitude,
//                       a.id           AS address_id,
//                       a.city_name,
//                       a.street_name,
//                       a.home_number,
//                       a.home_letter
//                FROM store AS st
//                         JOIN address a on a.id = st.address_id
//                ORDER BY st.location <-> point(?, ?)
//                LIMIT 3
//                """;
//
//        return jdbcTemplate.query(sql, new StoreMapper(),
//                        location.longitude(),
//                        location.latitude())
//                .stream()
//                .peek(store -> store.setStorage(new Storage()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    @Cacheable("StoreRepository::getAllProducts")
//    public List<Product> getAllProducts() {
//
//        var sql = "SELECT p.id AS product_id, p.name, p.description, p.size FROM product AS p";
//
//        return jdbcTemplate.query(sql, new ProductMapper())
//                .stream()
//                .peek(product -> {
//                    jdbcTemplate.query("""
//                                    SELECT pi.ingredient_id, pi.amount
//                                    FROM product_ingredient AS pi
//                                    WHERE pi.product_id = ?;
//                                    """,
//                            resultSet -> {
//                                Map<UUID, Float> ingredientIdAmount = new HashMap<>();
//                                while (resultSet.next()) {
//                                    ingredientIdAmount.put(
//                                            resultSet.getObject("ingredient_id", UUID.class),
//                                            resultSet.getFloat("amount"));
//                                }
//                                return ingredientIdAmount;
//                            }, product.getId()).forEach((ingredientId, amount) -> product.getIngredientIdAmount().put(ingredientId, amount));
//                }).collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public UUID addCart(Cart cart) {
//
//        return jdbcTemplate.queryForObject("""
//                        INSERT INTO cart(client_id, store_id, order_price, status, is_paid, products)
//                        VALUES (?, ?, ?, ?, ?, ?)
//                        RETURNING id
//                        """,
//                UUID.class,
//                cart.getClientId(),
//                cart.getStoreId(),
//                cart.getOrderPrice(),
//                cart.getStatus().ordinal(),
//                cart.isPaid(),
//                new SqlParameterValue(ARRAY, cart.getProducts()
//                        .stream()
//                        .map(Product::getId)
//                        .toArray(UUID[]::new)));
//    }
//
//    @Override
//    @Transactional
//    public boolean updateCart(Cart cart) {
//        var sql = """
//                UPDATE cart
//                SET client_id = ?,
//                    store_id = ?,
//                    order_price = ?,
//                    status = ?,
//                    is_paid = ?,
//                    products = ?
//                WHERE id = ?;
//                """;
//        return jdbcTemplate.update(sql,
//                cart.getClientId(),
//                cart.getStoreId(),
//                cart.getOrderPrice(),
//                cart.getStatus().ordinal(),
//                cart.isPaid(),
//                new SqlParameterValue(ARRAY, cart.getProducts()
//                        .stream()
//                        .map(Product::getId)
//                        .toArray(UUID[]::new)),
//                cart.getId()) >= 1;
//    }
//
//    @Override
//    @Transactional
//    public List<Cart> getCartsByClientId(long id) {
//
//        var sql = "SELECT * FROM cart WHERE client_id = ?";
//
//        return jdbcTemplate.query(sql, new CartMapper(), id)
//                .stream()
//                .peek(cart -> {
//                    cart.setProducts(getAllProductsByIds(cart.getProducts()
//                            .stream()
//                            .map(Product::getId)
//                            .collect(Collectors.toList())));
//                }).collect(Collectors.toList());
//    }
//
//    private List<Product> getAllProductsByIds(List<UUID> ids) {
//        var inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
//        return jdbcTemplate.query(
//                        String.format("SELECT p.id AS product_id, p.name, p.description, p.size FROM product AS p WHERE id IN (%s)", inSql),
//                        new ProductMapper(),
//                        ids.toArray())
//                .stream()
//                .peek(product -> {
//                    jdbcTemplate.query("""
//                                    SELECT pi.ingredient_id, pi.amount
//                                    FROM product_ingredient AS pi
//                                    WHERE pi.product_id = ?;
//                                    """,
//                            resultSet -> {
//                                Map<UUID, Float> ingredientIdAmount = new HashMap<>();
//                                while (resultSet.next()) {
//                                    ingredientIdAmount.put(
//                                            resultSet.getObject("ingredient_id", UUID.class),
//                                            resultSet.getFloat("amount"));
//                                }
//                                return ingredientIdAmount;
//                            }, product.getId()).forEach((ingredientId, amount) -> product.getIngredientIdAmount().put(ingredientId, amount));
//                }).collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public List<Cart> getCartsByStoreIdAndStatus(UUID id, Status status) {
//
//        var sql = """
//                SELECT * FROM cart
//                WHERE store_id = ? AND status = ?
//                """;
//
//        return jdbcTemplate.query(sql, new CartMapper(), id, status.ordinal())
//                .stream()
//                .peek(cart -> {
//                    cart.setProducts(getAllProductsByIds(cart.getProducts()
//                            .stream()
//                            .map(Product::getId)
//                            .collect(Collectors.toList())));
//                }).collect(Collectors.toList());
//    }
