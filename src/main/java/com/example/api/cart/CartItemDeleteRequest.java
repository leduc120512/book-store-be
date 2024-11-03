package com.example.api.cart;

public class CartItemDeleteRequest {
    private int cartItemId;
    private int userId;

    // Constructor
    public CartItemDeleteRequest(int cartItemId, int userId) {
        this.cartItemId = cartItemId;
        this.userId = userId;
    }

    // Getters and Setters
    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}