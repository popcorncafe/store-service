//package com.popcorncafe.storeservice.controller;
//
//import com.popcorncafe.storeservice.dao.entity.Cart;
//import com.popcorncafe.storeservice.dao.entity.Product;
//import com.popcorncafe.storeservice.dao.entity.Status;
//import com.popcorncafe.storeservice.dao.entity.Ingredient;
//import com.popcorncafe.storeservice.dto.Location;
//import com.popcorncafe.storeservice.dao.entity.Storage;
//import com.popcorncafe.storeservice.dao.entity.Store;
//import com.popcorncafe.storeservice.service.StoreService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping
//public class StoreController {
//
//    private final StoreService storeService;
//
//    @GetMapping
//    public ResponseEntity<List<Store>> getAllStores() {
//        return storeService.getAllStores();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Store> getStoreById(@PathVariable("id") UUID id) {
//        return storeService.getStoreById(id);
//    }
//
//    @PutMapping
//    public ResponseEntity<UUID> addStore(@RequestBody Store store) {
//        return storeService.addStore(store);
//    }
//
//    @PatchMapping
//    public ResponseEntity<Boolean> updateStore(@RequestBody Store store) {
//        return storeService.updateStore(store);
//    }
//
//    @PatchMapping("/storages")
//    public ResponseEntity<Boolean> updateStorage(@RequestBody Storage storage) {
//        return storeService.updateStorage(storage);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Boolean> deleteStore(@PathVariable("id") UUID id) {
//        return storeService.deleteStore(id);
//    }
//
//    @GetMapping("/ingredients")
//    public ResponseEntity<List<Ingredient>> getAllIngredients() {
//        return storeService.getAllIngredients();
//    }
//
//
//    @PostMapping("/location")
//    ResponseEntity<List<Store>> getStoresByLocation(@RequestBody Location location) {
//        return storeService.getStoresByLocation(location);
//    }
//
//    @GetMapping("/{id}/products")
//    ResponseEntity<List<Product>> getProductsByStoreId(@PathVariable("id") UUID id) {
//        return storeService.getProductsByStoreId(id);
//    }
//
//    @PutMapping("/carts")
//    ResponseEntity<UUID> addCart(@RequestBody Cart cart) {
//        return storeService.addCart(cart);
//    }
//
//    @PatchMapping("/carts")
//    ResponseEntity<Boolean> updateCart(@RequestBody Cart cart) {
//        return storeService.updateCart(cart);
//    }
//
//    @GetMapping("/carts/{id}")
//    public ResponseEntity<List<Cart>> getCartsByClientId(@PathVariable("id") long id) {
//        return storeService.getCartsByClientId(id);
//    }
//
//    @GetMapping("/{id}/carts/{status}")
//    public ResponseEntity<List<Cart>> getCartsByStoreIdAndStatus(
//            @PathVariable("id") UUID id,
//            @PathVariable("status") Status status) {
//        return storeService.getCartsByStoreIdAndStatus(id, status);
//    }
//}