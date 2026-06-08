package org.lab4;

public class Product {
    private final String id;
    private final String name;
    private final double price;
    private final String brand;
    private final String description;
    private final String imagePath;

    public Product(String id, String name, double price, String brand, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}
