package com.popcorncafe.storeservice.service.mapper;

import com.popcorncafe.storeservice.repository.model.Store;
import com.popcorncafe.storeservice.service.dto.StoreDto;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper implements Mapper<Store, StoreDto> {

    private final AddressMapper addressMapper;

    public StoreMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Override
    public Store toModel(StoreDto dto) {
        return new Store(
                dto.id(),
                addressMapper.toModel(dto.address()),
                dto.location()
        );
    }

    @Override
    public StoreDto toDto(Store model) {
        return new StoreDto(
                model.storeId(),
                addressMapper.toDto(model.address()),
                model.location()
        );
    }
}
