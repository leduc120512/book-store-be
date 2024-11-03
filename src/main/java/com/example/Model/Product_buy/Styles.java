package com.example.Model.Product_buy;

public class Styles {
    private int id;
    private int product_id;
    private String styles_name;
    private float price;

    public Styles(int id, int product_id, String styles_name, float price) {
        this.id = id;
        this.product_id = product_id;
        this.styles_name = styles_name;
        this.price = price;
    }

    public Styles(int product_id, String styles_name, float price) {
        this.product_id = product_id;
        this.styles_name = styles_name;
        this.price = price;
    }

    public Styles() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getStyles_name() {
        return styles_name;
    }

    public void setStyles_name(String styles_name) {
        this.styles_name = styles_name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
}
