package io.github.artemnefedov.entity;

import java.io.Serializable;
import java.util.UUID;

public record Address (
        UUID id,
        String cityName,
        String streetName,
        int homeNumber,
        String homeLetter
) implements Serializable {
}

