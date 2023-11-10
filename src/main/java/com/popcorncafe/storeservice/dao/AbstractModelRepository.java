package com.popcorncafe.storeservice.dao;

import com.popcorncafe.storeservice.dao.model.Model;
import com.popcorncafe.storeservice.dto.Page;

import java.util.List;
import java.util.UUID;

public interface AbstractModelRepository<T extends Model> {
    List<T> findByStoreId(UUID id, Page page);
}
