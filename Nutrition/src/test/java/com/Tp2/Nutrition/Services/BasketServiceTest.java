package com.Tp2.Nutrition.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Tp2.Nutrition.Data.Entity.BasketEntity;
import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Exceptions.InvalidBasketRequestException;
import com.Tp2.Nutrition.Repository.BasketRepository;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private NutritionService nutritionService;

    @InjectMocks
    private BasketService basketService;

    @Test
    void addToBucketCreatesBasketWhenUserDoesNotExist() {
        String email = "user@test.com";
        List<String> barcodes = List.of("123", "456");

        BasketEntity createdBasket = new BasketEntity();
        createdBasket.setUserEmail(email);
        createdBasket.setProductBarcodes(new ArrayList<>());

        when(basketRepository.findByUserEmail(email)).thenReturn(Optional.empty());
        when(basketRepository.save(org.mockito.ArgumentMatchers.any(BasketEntity.class)))
            .thenReturn(createdBasket);

        basketService.addToBucket(email, barcodes);

        ArgumentCaptor<BasketEntity> basketCaptor = ArgumentCaptor.forClass(BasketEntity.class);
        verify(basketRepository, times(2)).save(basketCaptor.capture());

        List<BasketEntity> savedBaskets = basketCaptor.getAllValues();
        assertEquals(email, savedBaskets.get(0).getUserEmail());
        assertEquals(List.of(), savedBaskets.get(0).getProductBarcodes());
        assertEquals(barcodes, savedBaskets.get(1).getProductBarcodes());
    }

    @Test
    void getBasketProductsReturnsResolvedProducts() {
        String email = "user@test.com";
        BasketEntity basket = new BasketEntity();
        basket.setUserEmail(email);
        basket.setProductBarcodes(List.of("123", "456"));

        ResponseModel first = new ResponseModel();
        first.barCode = "123";
        ResponseModel second = new ResponseModel();
        second.barCode = "456";

        when(basketRepository.findByUserEmail(email)).thenReturn(Optional.of(basket));
        when(nutritionService.getNutritionData("123")).thenReturn(first);
        when(nutritionService.getNutritionData("456")).thenReturn(second);

        List<ResponseModel> result = basketService.getBasketProducts(email);

        assertEquals(2, result.size());
        assertSame(first, result.get(0));
        assertSame(second, result.get(1));
    }

    @Test
    void removeFromBasketDeletesBarcodeWhenBasketExists() {
        String email = "user@test.com";
        BasketEntity basket = new BasketEntity();
        basket.setUserEmail(email);
        basket.setProductBarcodes(new ArrayList<>(List.of("123", "456")));

        when(basketRepository.findByUserEmail(email)).thenReturn(Optional.of(basket));

        basketService.removeFromBasket(email, "123");

        assertEquals(List.of("456"), basket.getProductBarcodes());
        verify(basketRepository).save(basket);
    }

    @Test
    void deleteBasketDoesNothingWhenBasketDoesNotExist() {
        String email = "user@test.com";
        when(basketRepository.findByUserEmail(email)).thenReturn(Optional.empty());

        basketService.deleteBasket(email);

        verify(basketRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void addToBucketThrowsWhenBarcodesContainBlankValue() {
        InvalidBasketRequestException exception = assertThrows(
            InvalidBasketRequestException.class,
            () -> basketService.addToBucket("user@test.com", List.of("123", " "))
        );

        assertEquals(
            "Chaque code-barres du panier doit etre renseigne.",
            exception.getMessage());
        verify(basketRepository, never()).findByUserEmail(org.mockito.ArgumentMatchers.anyString());
    }
}
