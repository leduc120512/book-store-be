package com.example.Model.Category;

public class Categories {
    private int Categories;
    private String Categories_name;

    public Categories(int Categories, String Categories_name) {
        this.Categories = Categories;
        this.Categories_name = Categories_name;
    }

    public Categories(String Categories_name) {
        this.Categories_name = Categories_name;
    }

    public Categories() {
    }

    public int getCategories() {
        return Categories;
    }

    public void setCategories(int Categories) {
        this.Categories = Categories;
    }

    public String getCategories_name() {
        return Categories_name;
    }

    public void setCategories_name(String Categories_name) {
        this.Categories_name = Categories_name;
    }
    
    
}
