package com.popcorncafe.storeservice.dao;

import com.popcorncafe.storeservice.dao.model.Model;

import java.util.Optional;
import java.util.UUID;

public interface AbstractModelRepository <T extends Model>{
    Optional<T> findByStoreId(UUID id);
}
