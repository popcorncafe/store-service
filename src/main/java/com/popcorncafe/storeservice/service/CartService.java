package com.popcorncafe.storeservice.service;

import com.popcorncafe.storeservice.service.dto.CartDto;
import com.popcorncafe.storeservice.service.dto.Page;

import java.util.List;
import java.util.UUID;

public interface CartService {

    List<CartDto> getCarts(Page page);

    List<CartDto> getByStore(UUID id, Page page);

    List<CartDto> getByClient(long id);

    CartDto getCart(UUID id);

    CartDto createCart(CartDto cartDto);

    boolean updateCart(CartDto cartDto);

    boolean deleteCart(UUID id);
}
