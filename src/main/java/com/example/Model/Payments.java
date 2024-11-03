package com.example.Model;

import java.security.Timestamp;

public class Payments {
    private int payment_id;
    private int order_id;
    private String payMent_method;
    private String payment_Status;
    private float amount;
    private Timestamp create_at;

    public Payments() {
    }

    
    
    public Payments(int order_id, String payMent_method, String payment_Status, float amount, Timestamp create_at) {
        this.order_id = order_id;
        this.payMent_method = payMent_method;
        this.payment_Status = payment_Status;
        this.amount = amount;
        this.create_at = create_at;
    }

    
    
    public Payments(int payment_id, int order_id, String payMent_method, String payment_Status, float amount, Timestamp create_at) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.payMent_method = payMent_method;
        this.payment_Status = payment_Status;
        this.amount = amount;
        this.create_at = create_at;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getPayMent_method() {
        return payMent_method;
    }

    public void setPayMent_method(String payMent_method) {
        this.payMent_method = payMent_method;
    }

    public String getPayment_Status() {
        return payment_Status;
    }

    public void setPayment_Status(String payment_Status) {
        this.payment_Status = payment_Status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }
    
    
}
