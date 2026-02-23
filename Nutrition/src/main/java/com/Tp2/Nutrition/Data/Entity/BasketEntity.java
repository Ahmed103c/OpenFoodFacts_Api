package com.Tp2.Nutrition.Data.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "Panier")
public class BasketEntity {

    @Id
    @Column(nullable = false)
    private String userEmail;

    @ElementCollection
    @CollectionTable(name = "basket_products", joinColumns = @JoinColumn(name = "basket_email"))
    @Column(name = "barcode")
    private List<String> productBarcodes = new ArrayList<>();

    // Getters and Setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<String> getProductBarcodes() {
        return productBarcodes;
    }

    public void setProductBarcodes(List<String> productBarcodes) {
        this.productBarcodes = productBarcodes;
    }
    
    
}
