package com.popcorncafe.storeservice.dao;

import com.popcorncafe.storeservice.dao.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductDao extends AbstractModelDao<Product> {
    List<Product> findByCartId(UUID id);
}
