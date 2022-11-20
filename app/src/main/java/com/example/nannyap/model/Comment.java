package com.example.nannyap.model;

public class Comment {
    private String id, name, comment, date, rate, imageUrl;

    public Comment() {
    }

    public Comment(String id, String name, String date, String comment, String rate, String imageUrl) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.comment = comment;
        this.rate = rate;
        this.imageUrl = imageUrl;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
