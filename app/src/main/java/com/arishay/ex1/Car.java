package com.arishay.ex1;

public class Car {
    public String id;
    public String make;
    public String model;
    public int year;
    public String color;

    public Car() {
        // Required by Firebase
    }

    public Car(String id, String make, String model, int year, String color) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
    }
}
