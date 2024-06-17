package com.popcorncafe.storeservice.controller;

import com.popcorncafe.storeservice.service.AddressService;
import com.popcorncafe.storeservice.service.dto.AddressDto;
import com.popcorncafe.storeservice.service.dto.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{size}/{num}")
    public ResponseEntity<List<AddressDto>> getAddresses(
            @PathVariable("size") int size, @PathVariable("num") int num
    ) {
        return ResponseEntity.ok(addressService.getAddresses(new Page(size, num)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(addressService.getAddress(id));
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.createAddress(addressDto));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateAddress(@RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(addressDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAddress(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }


}
