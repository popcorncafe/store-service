package io.github.artemnefedov.service;

import io.github.artemnefedov.entity.Cart;
import io.github.artemnefedov.entity.Product;
import io.github.artemnefedov.entity.Status;
import io.github.artemnefedov.entity.Ingredient;
import io.github.artemnefedov.entity.Location;
import io.github.artemnefedov.entity.Storage;
import io.github.artemnefedov.entity.Store;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    ResponseEntity<List<Store>> getAllStores();
    ResponseEntity<Store> getStoreById(UUID id);
    ResponseEntity<UUID> addStore(Store store);
    ResponseEntity<Boolean> updateStore(Store store);
    ResponseEntity<Boolean> updateStorage(Storage storage);
    ResponseEntity<Boolean> deleteStore(UUID id);
    ResponseEntity<List<Ingredient>> getAllIngredients();
    ResponseEntity<List<Store>> getStoresByLocation(Location location);
    ResponseEntity<List<Product>> getProductsByStoreId(UUID id);
    ResponseEntity<UUID> addCart(Cart cart);
    ResponseEntity<Boolean> updateCart(Cart cart);
    ResponseEntity<List<Cart>> getCartsByClientId(long id);
    ResponseEntity<List<Cart>> getCartsByStoreIdAndStatus(UUID id, Status status);
}
