package com.example.Model.Voucher;

import java.sql.Date;

public class Vouchers {
    private int voucher_id;
    private String code;
    private float discount;
    private Date valid_from;
    private Date valid_until;
    private float min_order_value;
    private float max_discount; // Using Float to allow null values
    private int usage_limit;
    private Date created_at;
    private Date updated_at;

    public Vouchers(int voucher_id, String code, float discount, Date valid_from, Date valid_until, float min_order_value, float max_discount, int usage_limit, Date created_at, Date updated_at) {
        this.voucher_id = voucher_id;
        this.code = code;
        this.discount = discount;
        this.valid_from = valid_from;
        this.valid_until = valid_until;
        this.min_order_value = min_order_value;
        this.max_discount = max_discount;
        this.usage_limit = usage_limit;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Vouchers(String code, float discount, Date valid_from, Date valid_until, float min_order_value, float max_discount, int usage_limit, Date created_at, Date updated_at) {
        this.code = code;
        this.discount = discount;
        this.valid_from = valid_from;
        this.valid_until = valid_until;
        this.min_order_value = min_order_value;
        this.max_discount = max_discount;
        this.usage_limit = usage_limit;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Vouchers() {
    }

    public int getVoucher_id() {
        return voucher_id;
    }

    public void setVoucher_id(int voucher_id) {
        this.voucher_id = voucher_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public Date getValid_from() {
        return valid_from;
    }

    public void setValid_from(Date valid_from) {
        this.valid_from = valid_from;
    }

    public Date getValid_until() {
        return valid_until;
    }

    public void setValid_until(Date valid_until) {
        this.valid_until = valid_until;
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

    public int getUsage_limit() {
        return usage_limit;
    }

    public void setUsage_limit(int usage_limit) {
        this.usage_limit = usage_limit;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
    
}
