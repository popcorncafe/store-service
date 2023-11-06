package com.popcorncafe.storeservice.dto;

import java.io.Serializable;

public record Location(
        float longitude,
        float latitude
) implements Serializable {
}
