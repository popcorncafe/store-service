package io.github.artemnefedov.service.implementation;

import io.github.artemnefedov.entity.Cart;
import io.github.artemnefedov.entity.Product;
import io.github.artemnefedov.entity.Status;
import io.github.artemnefedov.entity.Ingredient;
import io.github.artemnefedov.entity.Location;
import io.github.artemnefedov.entity.Storage;
import io.github.artemnefedov.entity.Store;
import io.github.artemnefedov.exsception.ResourceNotFound;
import io.github.artemnefedov.repository.StoreRepository;
import io.github.artemnefedov.service.KafkaProducerService;
import io.github.artemnefedov.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.github.artemnefedov.entity.Status.IS_PREPARING;
import static io.github.artemnefedov.entity.Status.PAID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final KafkaProducerService kafkaProdService;

    @Override
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeRepository.getAllStores());
    }

    @Override
    public ResponseEntity<Store> getStoreById(UUID id) {
        return ResponseEntity.ok(storeRepository.getStoreById(id)
                .orElseThrow(() -> new ResourceNotFound("Could not find the Store for the given id.")));
    }

    @Override
    public ResponseEntity<UUID> addStore(Store store) {
        return ResponseEntity.ok(storeRepository.addStore(store));
    }

    @Override
    public ResponseEntity<Boolean> updateStore(Store store) {
        return ResponseEntity.ok(storeRepository.updateStore(store));
    }

    @Override
    public ResponseEntity<Boolean> updateStorage(Storage storage) {
        return ResponseEntity.ok(storeRepository.updateStorage(storage));
    }

    @Override
    public ResponseEntity<Boolean> deleteStore(UUID id) {
        return ResponseEntity.ok(storeRepository.deleteStore(id));
    }

    @Override
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(storeRepository.getAllIngredients());
    }

    @Override
    public ResponseEntity<List<Store>> getStoresByLocation(Location location) {
        var stores = storeRepository.getStoresByLocation(location);
        if (stores.isEmpty()) {
            throw new ResourceNotFound("Could not find the Store for the given location.");
        }
        return ResponseEntity.ok(stores);
    }

    @Override
    public ResponseEntity<List<Product>> getProductsByStoreId(UUID id) {
        var ingredientsInStorage = Objects.requireNonNull(getStoreById(id).getBody())
                .getStorage().getIngredientIDAmount();
        Predicate<Product> haveOnStorage = product -> {
            var ingredientsForProduct = product.getIngredientIdAmount();
            var productIngredientKeys = ingredientsForProduct.keySet();
            for (UUID ingredientId: productIngredientKeys) {
                if (!ingredientsInStorage.containsKey(ingredientId)) {
                    return false;
                }
                if (ingredientsInStorage.get(ingredientId) < ingredientsForProduct.get(ingredientId)) {
                    return false;
                }
            }
            return true;
        };

        return ResponseEntity.ok(storeRepository.getAllProducts()
                .parallelStream()
                .filter(haveOnStorage)
        .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<UUID> addCart(Cart cart) {
        return ResponseEntity.ok(storeRepository.addCart(cart));
    }

    @Override
    public ResponseEntity<Boolean> updateCart(Cart cart) {

        switch (cart.getStatus()) {

            case CREATED -> {

                if (cart.isPaid()) {
                    cart.setStatus(PAID);
                    updateCart(cart);
                } else {

                    kafkaProdService.send("info_table", cart.getId().toString(), cart);
                }
            }
            case PAID -> {

                cart.setStatus(IS_PREPARING);
                updateCart(cart);
            }
            case IS_PREPARING, IS_READY -> kafkaProdService.send("info_table", cart.getId().toString(), cart);
            case COMPLETE -> {

                kafkaProdService.send("info_table", cart.getId().toString(), cart);
                kafkaProdService.send("report", cart.getId().toString(), cart);
            }
        }
        return ResponseEntity.ok(storeRepository.updateCart(cart));
    }

    @Override
    public ResponseEntity<List<Cart>> getCartsByClientId(long id) {
        return ResponseEntity.ok(storeRepository.getCartsByClientId(id));
    }

    @Override
    public ResponseEntity<List<Cart>> getCartsByStoreIdAndStatus(UUID id, Status status) {
        return ResponseEntity.ok(storeRepository.getCartsByStoreIdAndStatus(id, status));
    }
}
