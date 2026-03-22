package com.Tp2.Nutrition.Services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.Tp2.Nutrition.Data.Dtos.OpenFoodFactsDto;
import com.Tp2.Nutrition.Data.Dtos.ProductDto;
import com.Tp2.Nutrition.Data.Entity.NutritionEntity;
import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Exceptions.ProductNotFoundException;
import com.Tp2.Nutrition.Repository.NutritionRepository;

@ExtendWith(MockitoExtension.class)
class NutritionServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private NutritionRepository nutritionRepository;

    @Mock
    private NutritionFactory nutritionFactory;

    @InjectMocks
    private NutritionService nutritionService;

    @Test
    void getNutritionDataReturnsExistingEntityFromRepository() {
        NutritionEntity entity = new NutritionEntity();
        entity.setBarcode("123");
        ResponseModel expected = new ResponseModel();
        expected.barCode = "123";

        when(nutritionRepository.findByBarcode("123")).thenReturn(Optional.of(entity));
        when(nutritionFactory.entityToResponseModel(entity)).thenReturn(expected);

        ResponseModel result = nutritionService.getNutritionData("123");

        assertSame(expected, result);
        verify(restTemplate, never()).getForObject(any(), eq(OpenFoodFactsDto.class));
    }

    @Test
    void getNutritionDataThrowsWhenBarcodeIsBlank() {
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
            () -> nutritionService.getNutritionData(" "));

        assertEquals("Le code-barres du produit est obligatoire.", exception.getMessage());
        verify(nutritionRepository, never()).findByBarcode(any());
        verify(restTemplate, never()).getForObject(any(), eq(OpenFoodFactsDto.class));
    }

    @Test
    void getNutritionDataFetchesFromApiWhenRepositoryIsEmpty() {
        String barcode = "456";
        OpenFoodFactsDto dto = new OpenFoodFactsDto();
        dto.product = new ProductDto();
        ResponseModel expected = new ResponseModel();
        expected.barCode = barcode;

        NutritionEntity entity = new NutritionEntity();
        entity.setBarcode(barcode);

        when(nutritionRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(
            "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json",
            OpenFoodFactsDto.class))
            .thenReturn(dto);
        when(nutritionFactory.dtoToResponseModel(dto, barcode)).thenReturn(expected);
        when(nutritionFactory.responseModelToEntity(expected)).thenReturn(entity);

        ResponseModel result = nutritionService.getNutritionData(barcode);

        assertSame(expected, result);
        verify(nutritionFactory).dtoToResponseModel(dto, barcode);
        verify(nutritionRepository).save(entity);
    }

    @Test
    void getNutritionDataThrowsWhenApiCallFails() {
        String barcode = "789";

        when(nutritionRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(
            "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json",
            OpenFoodFactsDto.class))
            .thenThrow(new RestClientException("boom"));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
            () -> nutritionService.getNutritionData(barcode));

        assertEquals(
            "Impossible de recuperer le produit pour le code-barres 789.",
            exception.getMessage());
    }
}
