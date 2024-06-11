package com.popcorncafe.storeservice.service.mapper;

import com.popcorncafe.storeservice.repository.model.Product;
import com.popcorncafe.storeservice.service.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements Mapper<Product, ProductDto> {

    @Override
    public Product toModel(ProductDto dto) {
        return null;
    }

    @Override
    public ProductDto toDto(Product model) {
        return new ProductDto(
                model.productId(),
                model.name(),
                model.description(),
                model.size().name()
        );
    }
}
