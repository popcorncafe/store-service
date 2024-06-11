package com.popcorncafe.storeservice.service.impl;

import com.popcorncafe.storeservice.repository.StoreRepository;
import com.popcorncafe.storeservice.repository.model.Store;
import com.popcorncafe.storeservice.service.StoreService;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.dto.StoreDto;
import com.popcorncafe.storeservice.service.mapper.StoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper mapper;
    private final Logger log = LoggerFactory.getLogger(StoreService.class);

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper mapper) {
        this.storeRepository = storeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<StoreDto> getStores(Page page) {
        return storeRepository.getAll(page).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "StoreService::get", key = "#id")
    public StoreDto getStore(UUID id) {
        return storeRepository.get(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Could not find the Store for the given id."));
    }

    @Override
    @CachePut(value = "StoreService::get", key = "#storeDto.id()")
    public StoreDto createStore(StoreDto storeDto) {
        var storeId = storeRepository.create(mapper.toModel(storeDto));
        return new StoreDto(
                storeId,
                storeDto.address(),
                storeDto.location()
        );
    }

    @Override
    @CacheEvict(value = "StoreService::get", key = "#storeDto.id()")
    public boolean updateStore(StoreDto storeDto) {
        return storeRepository.update(mapper.toModel(storeDto));
    }

    @Override
    public List<StoreDto> getStoresByLocation(Store.Location location) {
        return storeRepository.getByLocation(location).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @CacheEvict(value = "StoreService::get", key = "#id")
    public boolean deleteStore(UUID id) {
        return storeRepository.delete(id);
    }
}
