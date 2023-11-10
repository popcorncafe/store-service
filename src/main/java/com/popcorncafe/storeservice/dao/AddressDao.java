package com.popcorncafe.storeservice.dao;

import com.popcorncafe.storeservice.dao.model.Address;

import java.util.Optional;
import java.util.UUID;

public interface AddressDao extends AbstractModelDao<Address> {
    Optional<Address> findByStoreId(UUID id);
}
