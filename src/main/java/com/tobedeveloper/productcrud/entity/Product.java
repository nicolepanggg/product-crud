package com.tobedeveloper.productcrud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //Marked as a JPA entity, corresponding to the product table in PostgreSQL
public class Product {

    @Id //define the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //uses PostgreSQL's sequence to automatically increment.
    private Long id;
    private String name;
    private double price;
    private String description;

    // No-argument constructor
    public Product() {}

    // No-argument constructor
    public Product(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}