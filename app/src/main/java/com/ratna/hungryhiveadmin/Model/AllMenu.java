package com.ratna.hungryhiveadmin.Model;

import java.io.Serializable;

public class AllMenu implements Serializable {
    private String id; // Firebase key
    private String foodName;
    private String foodPrice;
    private String foodDescription;
    private String foodIngredients;
    private String foodImage;

    // No-argument constructor required for Firebase deserialization
    public AllMenu() {}

    // Parameterized constructor
    public AllMenu(String id, String foodName, String foodPrice, String foodDescription, String foodIngredients, String foodImage) {
        this.id = id;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodIngredients = foodIngredients;
        this.foodImage = foodImage;
    }

    // Getter and setter methods for each field

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodIngredients() {
        return foodIngredients;
    }

    public void setFoodIngredients(String foodIngredients) {
        this.foodIngredients = foodIngredients;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
