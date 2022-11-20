package com.example.nannyap.model;

public class ParentModel {
    private String id, name, email, city, street, home, imageUrl, phone;

    public ParentModel() {
    }

    public ParentModel(String id, String name, String email, String city, String street, String home, String imageUrl, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.city = city;
        this.street = street;
        this.home = home;
        this.imageUrl = imageUrl;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHome() {
        return home;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhone() {
        return phone;
    }
}
