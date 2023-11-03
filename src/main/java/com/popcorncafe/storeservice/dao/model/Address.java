package com.popcorncafe.storeservice.dao.model;

import java.util.UUID;

public record Address(
        UUID addressId,
        String city,
        String street,
        int homeNumber,
        String homeLetter
) implements Model {
}
