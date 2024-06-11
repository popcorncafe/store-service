package com.popcorncafe.storeservice.controller;


import com.popcorncafe.storeservice.service.CartService;
import com.popcorncafe.storeservice.service.dto.CartDto;
import com.popcorncafe.storeservice.service.dto.Page;
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
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{size}/{num}")
    public ResponseEntity<List<CartDto>> getCarts(
            @PathVariable("size") int size,
            @PathVariable("num") int num) {
        return ResponseEntity.ok(cartService.getCarts(new Page(size, num)));
    }

    @GetMapping("/store/{id}/{size}/{num}")
    public ResponseEntity<List<CartDto>> getByStoreId(
            @PathVariable("id") UUID id,
            @PathVariable("size") int size,
            @PathVariable("num") int num) {
        return ResponseEntity.ok(cartService.getByStore(id, new Page(size, num)));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<CartDto>> getByClientId(@PathVariable("id") long id) {
        return ResponseEntity.ok(cartService.getByClient(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @PostMapping
    public ResponseEntity<CartDto> createCart(@RequestBody CartDto cartDto) {
        return ResponseEntity.ok(cartService.createCart(cartDto));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateCart(@RequestBody CartDto cartDto) {
        return ResponseEntity.ok(cartService.updateCart(cartDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCart(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(cartService.deleteCart(id));
    }
}
