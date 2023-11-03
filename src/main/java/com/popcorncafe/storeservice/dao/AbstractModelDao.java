package com.popcorncafe.storeservice.dao;

import com.popcorncafe.storeservice.dao.model.Model;
import com.popcorncafe.storeservice.dto.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AbstractModelDao <T extends Model> {
    Optional<T> getById(UUID id);
    List<T> getAll(Page page);
    UUID save(T model);
    boolean update(T model);
    boolean delete(UUID id);
}
