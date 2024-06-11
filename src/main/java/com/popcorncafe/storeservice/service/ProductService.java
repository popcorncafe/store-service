package com.popcorncafe.storeservice.service;

import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.dto.ProductDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductService {
    List<ProductDto> getProducts(Page page);

    ProductDto getProduct(UUID id);

    ProductDto createProduct(ProductDto productDto, Map<UUID, Float> ingredients);

    boolean updateProduct(ProductDto productDto);

    boolean deleteProduct(UUID id);

    List<ProductDto> getByStore(UUID id);
}
