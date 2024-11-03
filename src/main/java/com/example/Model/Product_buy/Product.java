package com.example.Model.Product_buy;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private int stock;
    private int sold; // Add 'sold' property
    private String categoryName;
    private int quantityToReduce;
 private int Categories;
    public int getCategories() {
        return Categories;
    }

    public void setCategories(int Categories) {
        this.Categories = Categories;
    }
    public int getquantityToReduce() {
        return quantityToReduce;
    }

    // Getters và Setters
    public int getProductId() {
        return productId;
    }

    public void setquantityToReduce(int quantityToReduce) {
        this.quantityToReduce = quantityToReduce;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSold() { // Add getter for 'sold'
        return sold;
    }

    public void setSold(int sold) { // Add setter for 'sold'
        this.sold = sold;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
