package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.model.Model;
import com.popcorncafe.storeservice.service.dto.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AbstractRepository<T extends Model> {

    Optional<T> get(UUID id);

    List<T> getAll(Page page);

    UUID create(T model);

    boolean update(T model);

    boolean delete(UUID id);
}
