package com.example.Model.Order;

public class Order_items {
    private int order_item_id;
    private int order_id;
    private int product_id;
    private int variant_id;
    private int quantity;
    private float unit_price;
    private float total_price;

    public Order_items(int order_item_id, int order_id, int product_id, int variant_id, int quantity, float unit_price, float total_price) {
        this.order_item_id = order_item_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.variant_id = variant_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total_price = total_price;
    }

    public Order_items(int order_id, int product_id, int variant_id, int quantity, float unit_price, float total_price) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.variant_id = variant_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total_price = total_price;
    }

    public Order_items() {
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    
    
}
