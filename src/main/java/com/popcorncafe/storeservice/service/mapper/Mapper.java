package com.popcorncafe.storeservice.service.mapper;

import com.popcorncafe.storeservice.repository.model.Model;
import com.popcorncafe.storeservice.service.dto.Dto;

public interface Mapper<T extends Model, R extends Dto> {

    T toModel(R dto);

    R toDto(T model);
}

// <T extends Model> { <T, R>
//    Optional<T> getById(UUID id);
//    List<T> getAll(Page page);
//    UUID save(T model);
//    boolean update(T model);
//    boolean delete(UUID id);
