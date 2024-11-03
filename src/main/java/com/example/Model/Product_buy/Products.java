package com.example.Model.Product_buy;

import java.sql.Timestamp;

public class Products {
    private int id;
    private String name;
    private String description;
    private float price;
    private int category_id;
    private int stock;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Products(int id, String name, String description, float price, int category_id, int stock, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.stock = stock;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Products(String name, String description, float price, int category_id, int stock, Timestamp created_at, Timestamp updated_at) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category_id = category_id;
        this.stock = stock;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Products() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
    
    
    
}
