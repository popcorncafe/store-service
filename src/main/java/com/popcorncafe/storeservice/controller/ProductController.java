package com.popcorncafe.storeservice.controller;

import com.popcorncafe.storeservice.service.ProductService;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.dto.ProductDto;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{size}/{num}")
    public ResponseEntity<List<ProductDto>> getProducts(
            @PathVariable("size") int size,
            @PathVariable("num") int num) {
        return ResponseEntity.ok(productService.getProducts(new Page(size, num)));
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<List<ProductDto>> getByStoreId(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(productService.getByStore(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, @RequestBody Map<UUID, Float> ingredients) {
        return ResponseEntity.ok(productService.createProduct(productDto, ingredients));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(productDto));
    }

    @DeleteMapping("/id")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
