package com.example.nannyap.model;

public class RatingModel {
    private String ratingId,parentId, nannyId, rate;

    public RatingModel() {
    }

    public RatingModel(String ratingId, String parentId, String nannyId,String rate) {
        this.ratingId = ratingId;
        this.parentId = parentId;
        this.nannyId = nannyId;
        this.rate = rate;
    }

    public String getRatingId() {
        return ratingId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getNannyId() {
        return nannyId;
    }

    public String getRate() {
        return rate;
    }
}
