package com.popcorncafe.storeservice.service.dto;

import com.popcorncafe.storeservice.repository.model.Store;

import java.util.UUID;

public record StoreDto(UUID id, AddressDto address, Store.Location location) implements Dto {
}