package com.popcorncafe.storeservice.service.impl;

import com.popcorncafe.storeservice.exsception.ResourceNotFoundException;
import com.popcorncafe.storeservice.repository.AddressRepository;
import com.popcorncafe.storeservice.service.AddressService;
import com.popcorncafe.storeservice.service.dto.AddressDto;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.mapper.AddressMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper mapper;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
        this.mapper = new AddressMapper();
    }


    @Override
    public List<AddressDto> getAddresses(Page page) {
        return addressRepository.getAll(page).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public AddressDto getAddress(UUID id) {
        return addressRepository.get(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Сould not find an address with id: " + id.toString()));
    }

    @Override
    public AddressDto createAddress(AddressDto addressDto) {
        UUID addressId = addressRepository.create(mapper.toModel(addressDto));
        return new AddressDto(
                addressId,
                addressDto.city(),
                addressDto.street(),
                addressDto.homeNumber(),
                addressDto.homeLetter()
        );
    }

    @Override
    public Boolean updateAddress(AddressDto addressDto) {
        return addressRepository.update(mapper.toModel(addressDto));
    }

    @Override
    public Boolean deleteAddress(UUID id) {
        return addressRepository.delete(id);
    }
}
