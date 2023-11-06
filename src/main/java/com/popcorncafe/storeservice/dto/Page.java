package com.popcorncafe.storeservice.dto;

public record Page(
        int size,
        int number
) {
    public int offset() {
        return this.number() * this.size();
    }
}
