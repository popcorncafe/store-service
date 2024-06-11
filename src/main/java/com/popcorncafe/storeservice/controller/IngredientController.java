package com.popcorncafe.storeservice.controller;

import com.popcorncafe.storeservice.service.IngredientService;
import com.popcorncafe.storeservice.service.dto.IngredientDto;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{size}/{num}")
    public ResponseEntity<List<IngredientDto>> getIngredients(
            @PathVariable("size") int size,
            @PathVariable("num") int num) {
        return ResponseEntity.ok(ingredientService.getIngredients(new Page(size, num)));
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<Map<IngredientDto, Float>> getByStoreId(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ingredientService.getByStore(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ingredientService.getIngredient(id));
    }

    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody IngredientDto ingredientDto) {
        return ResponseEntity.ok(ingredientService.createIngredient(ingredientDto));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateIngredient(@RequestBody IngredientDto ingredientDto) {
        return ResponseEntity.ok(ingredientService.updateIngredient(ingredientDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteIngredient(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ingredientService.deleteIngredient(id));
    }

    @GetMapping("/amount/{id}")
    public ResponseEntity<Map<IngredientDto, Float>> getAmountByStore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ingredientService.getByStore(id));
    }

    @PostMapping("/amount/{id}")
    public ResponseEntity<Boolean> updateAmountByStore(@RequestBody Map<UUID, Float> amount, @PathVariable("id") UUID id) {
        return ResponseEntity.ok(ingredientService.updateAmountByStore(amount, id));
    }
}
