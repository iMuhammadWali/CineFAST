package com.example.assignment_2;

public class Snack {
    int imageSrc;
    String name;
    String details;
    float price;

    public int getImageSrc() {
        return imageSrc;
    }

    public Snack(int imageSrc, String name, String details, float price) {
        this.imageSrc = imageSrc;
        this.name = name;
        this.details = details;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public float getPrice() {
        return price;
    }

    public void setDetail(String detail) {
        this.details = detail;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
