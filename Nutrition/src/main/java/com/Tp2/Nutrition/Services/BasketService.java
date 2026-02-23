package com.Tp2.Nutrition.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Tp2.Nutrition.Data.Entity.BasketEntity;
import com.Tp2.Nutrition.Data.Entity.NutritionEntity;
import com.Tp2.Nutrition.Repository.BasketRepository;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final NutritionService nutritionService;
    private final NutritionFactory nutritionFactory;

    public BasketService(BasketRepository basketRepository, NutritionService nutritionService, NutritionFactory nutritionFactory) {
        this.basketRepository = basketRepository;
        this.nutritionService = nutritionService;
        this.nutritionFactory = nutritionFactory;
    }


    public void addToBucket(String email, List<String> barcodes) {
        BasketEntity basket = basketRepository.findByUserEmail(email).orElseGet(() -> {
            BasketEntity newBasket = new BasketEntity();
            newBasket.setUserEmail(email);
            newBasket.setProductBarcodes(new ArrayList<>());
            return basketRepository.save(newBasket);
        });

        List<String> existingBarcodes = basket.getProductBarcodes();
        existingBarcodes.addAll(barcodes);
        basketRepository.save(basket);
    }

    public List<NutritionEntity> getBasketProducts(String email) {
        BasketEntity basket = basketRepository.findByUserEmail(email).orElse(null);
        if (basket == null) return new ArrayList<>();

        return basket.getProductBarcodes().stream()
            .map(barcode -> nutritionFactory.responseModelToEntity(nutritionService.getNutritionData(barcode)))
            .collect(Collectors.toList());
    }

    public void removeFromBasket(String email, String barcode) {
        BasketEntity basket = basketRepository.findByUserEmail(email).orElse(null);
        if (basket != null) {
            basket.getProductBarcodes().remove(barcode);
            basketRepository.save(basket);
        }

    }

    public void deleteBasket(String email) {
        basketRepository.findByUserEmail(email).ifPresent(basketRepository::delete);
    }

}
