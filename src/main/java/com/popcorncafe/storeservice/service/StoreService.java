//package com.popcorncafe.storeservice.service;
//
//import com.popcorncafe.storeservice.dao.entity.Cart;
//import com.popcorncafe.storeservice.dao.entity.Product;
//import com.popcorncafe.storeservice.dao.entity.Status;
//import com.popcorncafe.storeservice.dao.entity.Ingredient;
//import com.popcorncafe.storeservice.dto.Location;
//import com.popcorncafe.storeservice.dao.entity.Storage;
//import com.popcorncafe.storeservice.dao.entity.Store;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.UUID;
//
//public interface StoreService {
//
//    ResponseEntity<List<Store>> getAllStores();
//    ResponseEntity<Store> getStoreById(UUID id);
//    ResponseEntity<UUID> addStore(Store store);
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
//}
