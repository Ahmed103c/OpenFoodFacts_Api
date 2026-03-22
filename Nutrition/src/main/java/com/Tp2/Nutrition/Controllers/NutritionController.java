package com.Tp2.Nutrition.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Services.NutritionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/Product")
@Tag(name = "Products", description = "Gestion des données nutritionnelles des produits")
public class NutritionController {

    private final NutritionService nutritionService;

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping("")
    @Operation(summary = "Récupérer les données nutritionnelles d'un produit par son code-barres")
    public ResponseModel getData(@RequestParam(required = true) String barcode) {
        return this.nutritionService.getNutritionData(barcode);
    }
}
