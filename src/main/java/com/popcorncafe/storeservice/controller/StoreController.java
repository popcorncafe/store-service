package com.popcorncafe.storeservice.controller;

import com.popcorncafe.storeservice.repository.model.Store;
import com.popcorncafe.storeservice.service.StoreService;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.dto.StoreDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{size}/{num}")
    public ResponseEntity<List<StoreDto>> getStores(@PathVariable("size") int size, @PathVariable("num") int num) {
        return ResponseEntity.ok(storeService.getStores(new Page(size, num)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(storeService.getStore(id));
    }

    @PostMapping
    public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto storeDto) {
        return ResponseEntity.ok(storeService.createStore(storeDto));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateStore(@RequestBody StoreDto storeDto) {
        return ResponseEntity.ok(storeService.updateStore(storeDto));
    }

    @GetMapping("/location/{longitude}/{latitude}")
    ResponseEntity<List<StoreDto>> getStoreByLocation(@PathVariable("longitude") float longitude, @PathVariable("latitude") float latitude) {
        return ResponseEntity.ok(storeService.getStoresByLocation(new Store.Location(longitude, latitude)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteStore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(storeService.deleteStore(id));
    }
//    @PatchMapping("/storages")
//    public ResponseEntity<Boolean> updateStorage(@RequestBody Storage storage) {
//        return storeService.updateStorage(storage);
//    }


    //    @GetMapping("/{id}/products")
//    ResponseEntity<List<Product>> getProductsByStoreId(@PathVariable("id") UUID id) {
//        return storeService.getProductsByStoreId(id);
//    }

//    @GetMapping("/{id}/carts/{status}")
//    public ResponseEntity<List<Cart>> getCartsByStoreIdAndStatus(
//            @PathVariable("id") UUID id,
//            @PathVariable("status") Status status) {
//        return storeService.getCartsByStoreIdAndStatus(id, status);
//    }
}