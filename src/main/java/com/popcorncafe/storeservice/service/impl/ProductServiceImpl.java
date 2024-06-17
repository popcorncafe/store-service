package com.popcorncafe.storeservice.service.impl;

import com.popcorncafe.storeservice.exsception.ResourceNotFoundException;
import com.popcorncafe.storeservice.repository.IngredientRepository;
import com.popcorncafe.storeservice.repository.ProductRepository;
import com.popcorncafe.storeservice.service.ProductService;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.dto.ProductDto;
import com.popcorncafe.storeservice.service.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final ProductMapper mapper;

    public ProductServiceImpl(
            ProductRepository productRepository, IngredientRepository ingredientRepository, ProductMapper mapper
    ) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductDto> getProducts(Page page) {
        return productRepository.getAll(page).stream().map(mapper::toDto).toList();
    }

    @Override
    public ProductDto getProduct(UUID id) {
        return productRepository.get(id).map(mapper::toDto).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find product with id: " + id.toString()));
    }

    @Override
    public ProductDto createProduct(ProductDto productDto, Map<UUID, Float> ingredients) {
        var product = mapper.toModel(productDto);

        ingredients.forEach((id, amount) -> {
            var ingredient = ingredientRepository.get(id).orElseThrow(
                    () -> new ResourceNotFoundException("Cannot find ingredient with id: " + id.toString()));
            product.ingredientAmount().put(ingredient, amount);
        });
        var productId = productRepository.create(product);

        return new ProductDto(productId, productDto.name(), product.description(), productDto.size());
    }

    @Override
    public boolean updateProduct(ProductDto productDto) {
        return productRepository.update(mapper.toModel(productDto));
    }

    @Override
    public boolean deleteProduct(UUID id) {
        return productRepository.delete(id);
    }

    @Override
    public List<ProductDto> getByStore(UUID id) {
        var products = productRepository.getAll(new Page(1000, 0));
        var storeIngredients = ingredientRepository.getByStore(id);
        return products.stream().filter(product -> {
            var productIngredients = product.ingredientAmount();
            return productIngredients.keySet().containsAll(storeIngredients.keySet()) &&
                    productIngredients.entrySet().stream().allMatch(
                            ingredients -> storeIngredients.get(ingredients.getKey().ingredientId()) >=
                                    ingredients.getValue());
        }).map(mapper::toDto).toList();
    }
}
