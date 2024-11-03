package com.example.Model.Reviews;

import java.security.Timestamp;

public class Reviews {
    private int reviews;
    private int user_id;
    private int product_id;
    private int rating;
    private String title;
    private String comment;
    private Timestamp create_at;

    public Reviews(int reviews, int user_id, int product_id, int rating, String title, String comment, Timestamp create_at) {
        this.reviews = reviews;
        this.user_id = user_id;
        this.product_id = product_id;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.create_at = create_at;
    }

    public Reviews(int user_id, int product_id, int rating, String title, String comment, Timestamp create_at) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.create_at = create_at;
    }

    public Reviews() {
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }
    
    
}
