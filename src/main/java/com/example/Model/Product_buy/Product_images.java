package com.example.Model.Product_buy;

import java.sql.Date;

public class Product_images {
    private int image_id;
    private int product_id;
    private String image_url;
    private boolean is_primary;
    private Date created_at;

    public Product_images() {
    }

    public Product_images(int product_id, String image_url, boolean is_primary, Date created_at) {
        this.product_id = product_id;
        this.image_url = image_url;
        this.is_primary = is_primary;
        this.created_at = created_at;
    }

    public Product_images(int image_id, int product_id, String image_url, boolean is_primary, Date created_at) {
        this.image_id = image_id;
        this.product_id = product_id;
        this.image_url = image_url;
        this.is_primary = is_primary;
        this.created_at = created_at;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isIs_primary() {
        return is_primary;
    }

    public void setIs_primary(boolean is_primary) {
        this.is_primary = is_primary;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

}
