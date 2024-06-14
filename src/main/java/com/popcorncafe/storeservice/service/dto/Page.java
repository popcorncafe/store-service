package com.popcorncafe.storeservice.service.dto;

public record Page(
        int size,
        int number
) {
    public int offset() {
        return number() * size();
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(String.format("%d%d", size(), number()));
    }
}