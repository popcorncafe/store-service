//package com.popcorncafe.storeservice.service.implementation;
//
//import com.popcorncafe.storeservice.exsception.ResourceNotFoundException;
//import com.popcorncafe.storeservice.dao.StoreDao;
//import com.popcorncafe.storeservice.service.StoreService;
//import com.popcorncafe.storeservice.dao.entity.Cart;
//import com.popcorncafe.storeservice.dao.entity.Product;
//import com.popcorncafe.storeservice.dao.entity.Status;
//import com.popcorncafe.storeservice.dao.entity.Ingredient;
//import com.popcorncafe.storeservice.dto.Location;
//import com.popcorncafe.storeservice.dao.entity.Storage;
//import com.popcorncafe.storeservice.dao.entity.Store;
//import com.popcorncafe.storeservice.service.KafkaProducerService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//import static com.popcorncafe.storeservice.dao.entity.Status.IS_PREPARING;
//import static com.popcorncafe.storeservice.dao.entity.Status.PAID;
//
//@Service
//public class StoreServiceImpl implements StoreService {
//
//    private final StoreDao storeDao;
//    private final KafkaProducerService kafkaProdService;
//    private final Logger log;
//
//    public StoreServiceImpl(StoreDao storeDao, KafkaProducerService kafkaProdService) {
//        this.storeDao = storeDao;
//        this.kafkaProdService = kafkaProdService;
//        this.log = LoggerFactory.getLogger(StoreServiceImpl.class);
//    }
//
//    @Override
//    public ResponseEntity<List<Store>> getAllStores() {
//        return ResponseEntity.ok(storeDao.getAllStores());
//    }
//
//    @Override
//    public ResponseEntity<Store> getStoreById(UUID id) {
//        return ResponseEntity.ok(storeDao.getStoreById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Could not find the Store for the given id.")));
//    }
//
//    @Override
//    public ResponseEntity<UUID> addStore(Store store) {
//        return ResponseEntity.ok(storeDao.addStore(store));
//    }
//
//    @Override
//    public ResponseEntity<Boolean> updateStore(Store store) {
//        return ResponseEntity.ok(storeDao.updateStore(store));
//    }
//
//    @Override
//    public ResponseEntity<Boolean> updateStorage(Storage storage) {
//        return ResponseEntity.ok(storeDao.updateStorage(storage));
//    }
//
//    @Override
//    public ResponseEntity<Boolean> deleteStore(UUID id) {
//        return ResponseEntity.ok(storeDao.deleteStore(id));
//    }
//
//    @Override
//    public ResponseEntity<List<Ingredient>> getAllIngredients() {
//        return ResponseEntity.ok(storeDao.getAllIngredients());
//    }
//
//    @Override
//    public ResponseEntity<List<Store>> getStoresByLocation(Location location) {
//        var stores = storeDao.getStoresByLocation(location);
//        if (stores.isEmpty()) {
//            throw new ResourceNotFoundException("Could not find the Store for the given location.");
//        }
//        return ResponseEntity.ok(stores);
//    }
//
//    @Override
//    public ResponseEntity<List<Product>> getProductsByStoreId(UUID id) {
//        var ingredientsInStorage = Objects.requireNonNull(getStoreById(id).getBody())
//                .getStorage().getIngredientIDAmount();
//        Predicate<Product> haveOnStorage = product -> {
//            var ingredientsForProduct = product.getIngredientIdAmount();
//            var productIngredientKeys = ingredientsForProduct.keySet();
//            for (UUID ingredientId: productIngredientKeys) {
//                if (!ingredientsInStorage.containsKey(ingredientId)) {
//                    return false;
//                }
//                if (ingredientsInStorage.get(ingredientId) < ingredientsForProduct.get(ingredientId)) {
//                    return false;
//                }
//            }
//            return true;
//        };
//
//        return ResponseEntity.ok(storeDao.getAllProducts()
//                .parallelStream()
//                .filter(haveOnStorage)
//        .collect(Collectors.toList()));
//    }
//
//    @Override
//    public ResponseEntity<UUID> addCart(Cart cart) {
//        return ResponseEntity.ok(storeDao.addCart(cart));
//    }
//
//    @Override
//    public ResponseEntity<Boolean> updateCart(Cart cart) {
//
//        switch (cart.getStatus()) {
//
//            case CREATED -> {
//
//                if (cart.isPaid()) {
//                    cart.setStatus(PAID);
//                    updateCart(cart);
//                } else {
//
//                    kafkaProdService.send("info_table", cart.getId().toString(), cart);
//                }
//            }
//            case PAID -> {
//
//                cart.setStatus(IS_PREPARING);
//                updateCart(cart);
//            }
//            case IS_PREPARING, IS_READY -> kafkaProdService.send("info_table", cart.getId().toString(), cart);
//            case COMPLETE -> {
//
//                kafkaProdService.send("info_table", cart.getId().toString(), cart);
//                kafkaProdService.send("report", cart.getId().toString(), cart);
//            }
//        }
//        return ResponseEntity.ok(storeDao.updateCart(cart));
//    }
//
//    @Override
//    public ResponseEntity<List<Cart>> getCartsByClientId(long id) {
//        return ResponseEntity.ok(storeDao.getCartsByClientId(id));
//    }
//
//    @Override
//    public ResponseEntity<List<Cart>> getCartsByStoreIdAndStatus(UUID id, Status status) {
//        return ResponseEntity.ok(storeDao.getCartsByStoreIdAndStatus(id, status));
//    }
//}
