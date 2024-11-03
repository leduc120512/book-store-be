package com.example.Model.Cart;

import java.sql.Date;

public class Review_images {
    private int review_image_id;
    private int review_id;
    private String image_url;
    private Date uploaded_at;

    public Review_images() {
    }

    
    
    public Review_images(int review_image_id, int review_id, String image_url, Date uploaded_at) {
        this.review_image_id = review_image_id;
        this.review_id = review_id;
        this.image_url = image_url;
        this.uploaded_at = uploaded_at;
    }

    public Review_images(int review_id, String image_url, Date uploaded_at) {
        this.review_id = review_id;
        this.image_url = image_url;
        this.uploaded_at = uploaded_at;
    }

    public int getReview_image_id() {
        return review_image_id;
    }

    public void setReview_image_id(int review_image_id) {
        this.review_image_id = review_image_id;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(Date uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    
    
}
