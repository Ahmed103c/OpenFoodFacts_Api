package com.Tp2.Nutrition.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Tp2.Nutrition.Data.Model.ResponseModel;
import com.Tp2.Nutrition.Services.BasketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/Basket")
@Tag(name = "Basket", description = "Gestion du panier des utilisateurs")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping("/add")
    @Operation(summary = "Ajouter un produit au panier d'un utilisateur")
    public void addToBasket(@RequestParam String email, @RequestBody List<String> barcodes) {
        this.basketService.addToBucket(email, barcodes);
    }

    
    @GetMapping("")
    @Operation(summary = "Récupérer les produits du panier d'un utilisateur par son email")
    public List<ResponseModel>  getData(@RequestParam(required = true) String email) {
        return this.basketService.getBasketProducts(email);
    }

    @DeleteMapping("/removeProduct")
    @Operation(summary = "Supprimer un produit du panier d'un utilisateur")
    public void removeProduct(@RequestParam String email, @RequestParam String barcode) {
        basketService.removeFromBasket(email, barcode);
    }

    @DeleteMapping("")
    @Operation(summary = "Supprimer le panier d'un utilisateur")
    public void deleteBasket(@RequestParam String email) {
        basketService.deleteBasket(email);
    }
}
