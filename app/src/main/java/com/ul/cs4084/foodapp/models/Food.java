package com.ul.cs4084.foodapp.models;

import java.io.Serializable;

public class Food implements Serializable {

    private String id;
    private String name;
    private String imageUrl;
    private String description;

    private String imagePlaceholder;
    private Double price;
    private int count;

    public Food(String id, String name, String imageUrl, String description, String imagePlaceholder, Double price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.imagePlaceholder = imagePlaceholder;
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


    public String getId() {
        return id;
    }

    public String getImagePlaceholder() {
        return imagePlaceholder;
    }

    public void setImagePlaceholder(String imagePlaceholder) {
        this.imagePlaceholder = imagePlaceholder;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
