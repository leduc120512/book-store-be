package com.example.Model.Product_buy;

public class Product_variants {
    private int variant_id;
    private int product_id;
    private int style_id;
    private int size_id;
    private int stock;
    private float price;

    public Product_variants() {
    }

    public Product_variants(int variant_id, int product_id, int style_id, int size_id, int stock, float price) {
        this.variant_id = variant_id;
        this.product_id = product_id;
        this.style_id = style_id;
        this.size_id = size_id;
        this.stock = stock;
        this.price = price;
    }

    public Product_variants(int product_id, int style_id, int size_id, int stock, float price) {
        this.product_id = product_id;
        this.style_id = style_id;
        this.size_id = size_id;
        this.stock = stock;
        this.price = price;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getStyle_id() {
        return style_id;
    }

    public void setStyle_id(int style_id) {
        this.style_id = style_id;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    
    
    
    
}
