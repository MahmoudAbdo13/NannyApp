package com.example.nannyap.model;

public class NannyModel {
    private String nannyId, name, email, city, street, home, bookingTime, language, nationality, bio, imageUrl, age, childrenAge, mobile;
    private float rate;
    private boolean specialNeedsChildren, available;

    public NannyModel() {

    }

    public NannyModel(String nannyId, String name, String email, String city, String street, String home, String imageUrl, String mobile, float rate) {
        this.nannyId = nannyId;
        this.name = name;
        this.email = email;
        this.city = city;
        this.street = street;
        this.home = home;
        this.imageUrl = imageUrl;
        this.mobile = mobile;
    }

    public NannyModel(String nannyId, String name, String email, String city, String street, String home, String bookingTime, String language, String nationality, String bio, String imageUrl, String age, String childrenAge, String mobile, boolean specialNeedsChildren, boolean available, float rate) {
        this.nannyId = nannyId;
        this.name = name;
        this.email = email;
        this.city = city;
        this.street = street;
        this.home = home;
        this.bookingTime = bookingTime;
        this.language = language;
        this.nationality = nationality;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.age = age;
        this.childrenAge = childrenAge;
        this.mobile = mobile;
        this.specialNeedsChildren = specialNeedsChildren;
        this.available = available;
        this.rate = rate;
    }

    public String getNannyId() {
        return nannyId;
    }

    public void setNannyId(String nannyId) {
        this.nannyId = nannyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getChildrenAge() {
        return childrenAge;
    }

    public void setChildrenAge(String childrenAge) {
        this.childrenAge = childrenAge;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isSpecialNeedsChildren() {
        return specialNeedsChildren;
    }

    public void setSpecialNeedsChildren(boolean specialNeedsChildren) {
        this.specialNeedsChildren = specialNeedsChildren;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
