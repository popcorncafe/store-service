package com.popcorncafe.storeservice.dao.impl;

import com.popcorncafe.storeservice.dao.ProductDao;
import com.popcorncafe.storeservice.dao.model.Product;
import com.popcorncafe.storeservice.dto.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductDaoImpl implements ProductDao {
    @Override
    public Optional<Product> getById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Product> getAll(Page page) {
        return null;
    }

    @Override
    public UUID save(Product model) {
        return null;
    }

    @Override
    public boolean update(Product model) {
        return false;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public Optional<Product> findByStoreId(UUID id) {
        return Optional.empty();
    }
}
