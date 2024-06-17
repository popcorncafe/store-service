package com.popcorncafe.storeservice.service.impl;

import com.popcorncafe.storeservice.exsception.ResourceNotFoundException;
import com.popcorncafe.storeservice.repository.CartRepository;
import com.popcorncafe.storeservice.service.CartService;
import com.popcorncafe.storeservice.service.dto.CartDto;
import com.popcorncafe.storeservice.service.dto.Page;
import com.popcorncafe.storeservice.service.mapper.CartMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper mapper;

    public CartServiceImpl(CartRepository cartRepository, CartMapper mapper) {
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CartDto> getCarts(Page page) {
        return cartRepository.getAll(page).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<CartDto> getByStore(UUID id, Page page) {
        return cartRepository.getByStore(id, page).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<CartDto> getByClient(long id) {
        return cartRepository.getByClient(id).stream().map(mapper::toDto).toList();
    }

    @Override
    public CartDto getCart(UUID id) {
        return cartRepository.get(id).map(mapper::toDto).orElseThrow(
                () -> new ResourceNotFoundException("Cannot find cart with id: " + id.toString()));
    }

    @Override
    public CartDto createCart(CartDto cartDto) {
        var cartId = cartRepository.create(mapper.toModel(cartDto));
        return new CartDto(
                cartId, cartDto.clientId(), cartDto.storeId(), cartDto.orderDate(), cartDto.orderPrice(),
                cartDto.status(), cartDto.products()
        );
    }

    @Override
    public boolean updateCart(CartDto cartDto) {

        //todo sand upd-> kafka

        return cartRepository.update(mapper.toModel(cartDto));
    }

    @Override
    public boolean deleteCart(UUID id) {
        return cartRepository.delete(id);
    }
}
