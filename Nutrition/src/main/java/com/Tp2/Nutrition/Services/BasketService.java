package com.Tp2.Nutrition.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Tp2.Nutrition.Data.Entity.BasketEntity;
import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Exceptions.InvalidBasketRequestException;
import com.Tp2.Nutrition.Repository.BasketRepository;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final NutritionService nutritionService;

    public BasketService(BasketRepository basketRepository, NutritionService nutritionService) {
        this.basketRepository = basketRepository;
        this.nutritionService = nutritionService;
    }


    public void addToBucket(String email, List<String> barcodes) {
        validateEmail(email);
        validateBarcodes(barcodes);

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

    public List<ResponseModel> getBasketProducts(String email) {
        validateEmail(email);

        BasketEntity basket = basketRepository.findByUserEmail(email).orElse(null);
        if (basket == null) return new ArrayList<>();

        return basket.getProductBarcodes().stream()
            .map(barcode -> nutritionService.getNutritionData(barcode)) 
            .collect(Collectors.toList());
    }
    public void removeFromBasket(String email, String barcode) {
        validateEmail(email);
        validateBarcode(barcode);

        BasketEntity basket = basketRepository.findByUserEmail(email).orElse(null);
        if (basket != null) {
            basket.getProductBarcodes().remove(barcode);
            basketRepository.save(basket);
        }

    }

    public void deleteBasket(String email) {
        validateEmail(email);
        basketRepository.findByUserEmail(email).ifPresent(basketRepository::delete);
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidBasketRequestException("L'email utilisateur est obligatoire.");
        }
    }

    private void validateBarcodes(List<String> barcodes) {
        if (barcodes == null || barcodes.isEmpty()) {
            throw new InvalidBasketRequestException("La liste des codes-barres ne peut pas etre vide.");
        }

        boolean containsInvalidBarcode = barcodes.stream()
            .anyMatch(barcode -> barcode == null || barcode.isBlank());

        if (containsInvalidBarcode) {
            throw new InvalidBasketRequestException("Chaque code-barres du panier doit etre renseigne.");
        }
    }

    private void validateBarcode(String barcode) {
        if (barcode == null || barcode.isBlank()) {
            throw new InvalidBasketRequestException("Le code-barres est obligatoire.");
        }
    }

}
