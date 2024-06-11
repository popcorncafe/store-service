package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.model.Store;

import java.util.List;

public interface StoreRepository extends AbstractRepository<Store> {
    List<Store> getByLocation(Store.Location location);
}
