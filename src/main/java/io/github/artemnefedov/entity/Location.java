package io.github.artemnefedov.entity;

import java.io.Serializable;

public record Location(
        float longitude,
        float latitude
) implements Serializable {
}
