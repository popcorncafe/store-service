package io.github.artemnefedov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Storage implements Serializable {
        private UUID id;
        private Map<UUID, Float> ingredientIDAmount;
}
