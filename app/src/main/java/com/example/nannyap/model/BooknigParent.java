package com.example.nannyap.model;

public class BooknigParent {
    String bookingId, nannyId, nannyName, status, imageUrl, startDate, endDate, type;

    public BooknigParent() {
    }

    public BooknigParent(String bookingId, String nannyId, String nannyName, String status, String imageUrl, String startDate, String endDate, String type) {
        this.bookingId = bookingId;
        this.nannyId = nannyId;
        this.nannyName = nannyName;
        this.status = status;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getNannyId() {
        return nannyId;
    }

    public void setNannyId(String nannyId) {
        this.nannyId = nannyId;
    }

    public String getNannyName() {
        return nannyName;
    }

    public void setNannyName(String nannyName) {
        this.nannyName = nannyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
