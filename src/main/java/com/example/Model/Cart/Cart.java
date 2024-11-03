package com.example.Model.Cart;

import java.security.Timestamp;

public class Cart {
    private int cart_id;
    private int user_id;
    private Timestamp create_at;

    public Cart() {
    }

    
    
    public Cart(int cart_id, int user_id, Timestamp create_at) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.create_at = create_at;
    }

    public Cart(int user_id, Timestamp create_at) {
        this.user_id = user_id;
        this.create_at = create_at;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }
    
    
}
