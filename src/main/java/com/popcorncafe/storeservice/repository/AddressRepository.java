package com.popcorncafe.storeservice.repository;

import com.popcorncafe.storeservice.repository.model.Address;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends AbstractRepository<Address> {
    Optional<Address> getByStoreId(UUID id);
}
