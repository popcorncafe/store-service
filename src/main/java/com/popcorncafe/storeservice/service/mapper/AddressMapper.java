package com.popcorncafe.storeservice.service.mapper;

import com.popcorncafe.storeservice.repository.model.Address;
import com.popcorncafe.storeservice.service.dto.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements Mapper<Address, AddressDto> {

    @Override
    public Address toModel(AddressDto dto) {
        return new Address(dto.id(), dto.city(), dto.street(), dto.homeNumber(),
                dto.homeLetter().isEmpty() ? " " : dto.homeLetter()
        );
    }

    @Override
    public AddressDto toDto(Address model) {
        return new AddressDto(model.addressId(), model.city(), model.street(), model.homeNumber(), model.homeLetter());
    }
}
