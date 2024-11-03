package com.example.Model.Product_buy;

public class Sizes {
    private  int size_id;
    private int product_id;
    private String size_name;

    public Sizes(int size_id, int product_id, String size_name) {
        this.size_id = size_id;
        this.product_id = product_id;
        this.size_name = size_name;
    }

    public Sizes(int product_id, String size_name) {
        this.product_id = product_id;
        this.size_name = size_name;
    }

    public Sizes() {
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }
    
    
}
