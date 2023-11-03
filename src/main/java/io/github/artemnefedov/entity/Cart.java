package io.github.artemnefedov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {

    private UUID id;
    private long clientId;
    private UUID storeId;
    private List<Product> products;
    private Instant orderDate;
    private float orderPrice;
    private Status status;
    private boolean isPaid;
}
