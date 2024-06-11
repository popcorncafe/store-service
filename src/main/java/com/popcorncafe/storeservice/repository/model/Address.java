package com.popcorncafe.storeservice.repository.model;

import java.util.UUID;

public record Address(
        UUID addressId,
        String city,
        String street,
        int homeNumber,
        String homeLetter
) implements Model {
}
