package com.popcorncafe.storeservice.service.dto;

import java.util.UUID;

public record AddressDto(UUID id, String city, String street, int homeNumber, String homeLetter) implements Dto {
}
