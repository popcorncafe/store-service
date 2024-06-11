package com.popcorncafe.storeservice.repository.model;

import java.util.UUID;

public record Store(
        UUID storeId,
        Address address,
        Location location


) implements Model {
    public record Location(float longitude, float latitude) {}
}
