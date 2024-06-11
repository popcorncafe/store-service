package com.popcorncafe.storeservice.service;

import com.popcorncafe.storeservice.repository.model.Store;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.dto.StoreDto;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    List<StoreDto> getStores(Page page);

    StoreDto getStore(UUID id);

    StoreDto createStore(StoreDto storeDto);

    boolean updateStore(StoreDto storeDto);

    List<StoreDto> getStoresByLocation(Store.Location location);

    boolean deleteStore(UUID id);

//    ResponseEntity<List<Store>> getAllStores();
//    ResponseEntity<Store> getStoreById(UUID id);
//    ResponseEntity<UUID> createStore(Store store);
//    ResponseEntity<Boolean> updateStore(Store store);
//    ResponseEntity<Boolean> updateStorage(Storage storage);
//    ResponseEntity<Boolean> deleteStore(UUID id);
//    ResponseEntity<List<Ingredient>> getAllIngredients();
//    ResponseEntity<List<Store>> getStoresByLocation(Location location);
//    ResponseEntity<List<Product>> getProductsByStoreId(UUID id);
//    ResponseEntity<UUID> addCart(Cart cart);
//    ResponseEntity<Boolean> updateCart(Cart cart);
//    ResponseEntity<List<Cart>> getCartsByClientId(long id);
//    ResponseEntity<List<Cart>> getCartsByStoreIdAndStatus(UUID id, Status status);
}
