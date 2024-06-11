package com.popcorncafe.storeservice.service;

import com.popcorncafe.storeservice.service.dto.AddressDto;
import com.popcorncafe.storeservice.service.dto.Page;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    List<AddressDto> getAddresses(Page page);

    AddressDto getAddress(UUID id);

    AddressDto createAddress(AddressDto addressDto);

    Boolean updateAddress(AddressDto addressDto);

    Boolean deleteAddress(UUID id);
}
