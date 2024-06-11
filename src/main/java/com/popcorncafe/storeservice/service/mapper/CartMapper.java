package com.popcorncafe.storeservice.service.mapper;

import com.popcorncafe.storeservice.repository.model.Cart;
import com.popcorncafe.storeservice.repository.model.Product;
import com.popcorncafe.storeservice.service.ProductService;
import com.popcorncafe.storeservice.service.dto.CartDto;
import org.springframework.stereotype.Component;

@Component
public class CartMapper implements Mapper<Cart, CartDto> {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public CartMapper(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public Cart toModel(CartDto dto) {
        return new Cart(
                dto.id(),
                dto.clientId(),
                dto.storeId(),
                dto.orderDate(),
                dto.orderPrice(),
                Cart.Status.valueOf(dto.status()),
                dto.products().stream()
                        .map(productService::getProduct)
                        .map(productMapper::toModel)
                        .toList()
        );
    }

    @Override
    public CartDto toDto(Cart model) {
        return new CartDto(
                model.cartId(),
                model.clientId(),
                model.storeId(),
                model.orderDate(),
                model.orderPrice(),
                model.status().name(),
                model.products().stream()
                        .map(Product::productId)
                        .toList()
        );
    }
}
