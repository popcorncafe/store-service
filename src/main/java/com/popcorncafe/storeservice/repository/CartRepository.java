package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.model.Cart;
import com.popcorncafe.storeservice.service.dto.Page;

import java.util.List;
import java.util.UUID;

public interface CartRepository extends AbstractRepository<Cart> {

    List<Cart> getByClient(long id);

    List<Cart> getByStore(UUID id, Page page);
}
