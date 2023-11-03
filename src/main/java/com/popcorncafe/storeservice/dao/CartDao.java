package com.popcorncafe.storeservice.dao;

import com.popcorncafe.storeservice.dao.model.Cart;

import java.util.List;
import java.util.Optional;

public interface CartDao extends AbstractModelDao<Cart>, AbstractModelRepository<Cart> {
    List<Cart> findByClientId(long id);
}
