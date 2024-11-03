package com.example.Model.Order;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int userId;
    private String status;
    private float totalPrice;
    private Boolean reviewed;
    private int voucherId;
    private String paymentStatus;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructor with all parameters
    public Order(int orderId, int userId, String status, float totalPrice, Boolean reviewed, int voucherId,
            String paymentStatus, Timestamp createdAt, Timestamp updatedAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.reviewed = reviewed;
        this.voucherId = voucherId;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // Constructor phù hợp với dữ liệu được sử dụng trong UserProfileServlet
    public Order(int orderId, BigDecimal totalPrice, String status, Timestamp createdAt, int productId, int quantity, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.totalPrice = totalPrice.floatValue();
        this.status = status;
        this.createdAt = createdAt;
        // Các trường khác có thể thêm sau hoặc đặt mặc định
    }
    // Constructor for creating a new order without orderId
    public Order(int userId, String status, float totalPrice, Boolean reviewed, int voucherId, String paymentStatus,
            Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.reviewed = reviewed;
        this.voucherId = voucherId;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Default constructor
    public Order() {
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed) {
        this.reviewed = reviewed;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}