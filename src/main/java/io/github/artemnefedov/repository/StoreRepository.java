package io.github.artemnefedov.repository;

import io.github.artemnefedov.entity.Cart;
import io.github.artemnefedov.entity.Product;
import io.github.artemnefedov.entity.Status;
import io.github.artemnefedov.entity.Ingredient;
import io.github.artemnefedov.entity.Location;
import io.github.artemnefedov.entity.Storage;
import io.github.artemnefedov.entity.Store;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository {

    List<Store> getAllStores();
    Optional<Store> getStoreById(UUID id);
    UUID addStore(Store store);
    boolean updateStore(Store store);
    boolean updateStorage(Storage storage);
    boolean deleteStore(UUID id);
    List<Ingredient> getAllIngredients();
    List<Store> getStoresByLocation(Location location);
    List<Product> getAllProducts();
    UUID addCart(Cart cart);
    boolean updateCart(Cart cart);
    List<Cart> getCartsByClientId(long id);
    List<Cart> getCartsByStoreIdAndStatus(UUID id, Status status);
}
