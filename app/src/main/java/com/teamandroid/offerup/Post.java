package com.teamandroid.offerup;

import java.util.ArrayList;
import java.util.List;

public class Post {

    private String description;
    private String itemName;
    private List<String> category;
    private String condition;
    private double price;
    private String image;

    //default constructor
    public Post() { }

    public Post(String description, String itemName, List<String> category, String condition, double price, String image) {
        this.description = description;
        this.itemName = itemName;
        this.category = category;
        this.condition = condition;
        this.price = price;
        this.image = image;
    }

    //Getters
    public String getDescription() {
        return description;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    //Setters
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public void setItemName(String newItemName) {
        itemName = newItemName;
    }

    public void setCategory(List<String> newCategory) {
        category = newCategory;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }

    public void setImage(String newImage) {
        image = newImage;
    }
}