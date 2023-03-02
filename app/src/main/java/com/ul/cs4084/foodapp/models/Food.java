package com.ul.cs4084.foodapp.models;

public class Food {

    private String name;
    private String imageUrl;
    private String description;
    private Double price;

    public Food(String name, String imageUrl, String description, Double price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl.trim();
    }

    public String getDescription() {
        return description;
    }

    public CharSequence getPrice() {
        return "€" + String.valueOf(price);
    }
}
