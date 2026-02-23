package com.Tp2.Nutrition.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Tp2.Nutrition.Data.Entity.NutritionEntity;
import com.Tp2.Nutrition.Services.BasketService;

@RestController
@RequestMapping("/api/Basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping("/add")
    public void addToBasket(@RequestParam String email, @RequestBody List<String> barcodes) {
        this.basketService.addToBucket(email, barcodes);
    }

    
    @GetMapping("")
    public List<NutritionEntity>  getData(@RequestParam(required = true) String email) {
        return this.basketService.getBasketProducts(email);
    }
}
