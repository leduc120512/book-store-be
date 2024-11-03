package com.example.Model.Voucher;

import java.sql.Date;

public class User_voucher {
    private int user_voucher_id;
    private int user_id;
    private int voucher_id;
    private boolean is_used;
    private Date used_at;
    private float discount_percent;
    private float discount_amount; // Using Float to allow null values
    private float min_order_value;
    private float max_discount; // Using Float to allow null values
    private Date valid_until;

    public User_voucher() {
    }

    public User_voucher(int user_voucher_id, int user_id, int voucher_id, boolean is_used, Date used_at, float discount_percent, float discount_amount, float min_order_value, float max_discount, Date valid_until) {
        this.user_voucher_id = user_voucher_id;
        this.user_id = user_id;
        this.voucher_id = voucher_id;
        this.is_used = is_used;
        this.used_at = used_at;
        this.discount_percent = discount_percent;
        this.discount_amount = discount_amount;
        this.min_order_value = min_order_value;
        this.max_discount = max_discount;
        this.valid_until = valid_until;
    }

    public User_voucher(int user_id, int voucher_id, boolean is_used, Date used_at, float discount_percent, float discount_amount, float min_order_value, float max_discount, Date valid_until) {
        this.user_id = user_id;
        this.voucher_id = voucher_id;
        this.is_used = is_used;
        this.used_at = used_at;
        this.discount_percent = discount_percent;
        this.discount_amount = discount_amount;
        this.min_order_value = min_order_value;
        this.max_discount = max_discount;
        this.valid_until = valid_until;
    }

    public int getUser_voucher_id() {
        return user_voucher_id;
    }

    public void setUser_voucher_id(int user_voucher_id) {
        this.user_voucher_id = user_voucher_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getVoucher_id() {
        return voucher_id;
    }

    public void setVoucher_id(int voucher_id) {
        this.voucher_id = voucher_id;
    }

    public boolean isIs_used() {
        return is_used;
    }

    public void setIs_used(boolean is_used) {
        this.is_used = is_used;
    }

    public Date getUsed_at() {
        return used_at;
    }

    public void setUsed_at(Date used_at) {
        this.used_at = used_at;
    }

    public float getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(float discount_percent) {
        this.discount_percent = discount_percent;
    }

    public float getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(float discount_amount) {
        this.discount_amount = discount_amount;
    }

    public float getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(float min_order_value) {
        this.min_order_value = min_order_value;
    }

    public float getMax_discount() {
        return max_discount;
    }

    public void setMax_discount(float max_discount) {
        this.max_discount = max_discount;
    }

    public Date getValid_until() {
        return valid_until;
    }

    public void setValid_until(Date valid_until) {
        this.valid_until = valid_until;
    }
    
    
}
