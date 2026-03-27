package com.example.assignment_2;

public class SelectedSnack {
    String name;
    float price;
    Integer quantity;
    float totalPrice;
    public SelectedSnack(String name, float price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        totalPrice = quantity * price;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }
}
