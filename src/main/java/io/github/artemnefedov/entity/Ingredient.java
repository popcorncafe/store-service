package io.github.artemnefedov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {

    private UUID id;
    private String name;
    private float unitPrice;
    private Measure measure;
}