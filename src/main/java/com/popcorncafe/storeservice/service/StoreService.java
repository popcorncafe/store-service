package com.popcorncafe.storeservice.service;

import com.popcorncafe.storeservice.repository.model.Store;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.dto.StoreDto;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    List<StoreDto> getStores(Page page);

    StoreDto getStore(UUID id);

    StoreDto createStore(StoreDto storeDto);

    boolean updateStore(StoreDto storeDto);

    List<StoreDto> getStoresByLocation(Store.Location location);

    boolean deleteStore(UUID id);
}
