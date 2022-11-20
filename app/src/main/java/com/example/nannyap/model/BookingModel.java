package com.example.nannyap.model;

public class BookingModel {
    String bookingId, nannyId, parentId, startDate, endDate, startTime, endTime, age, specialNeeds, type, note, status;

    public BookingModel() {
    }

    public BookingModel(String bookingId, String nannyId, String parentId, String startDate, String endDate, String startTime, String endTime, String age, String specialNeeds, String type, String note, String status) {
        this.bookingId = bookingId;
        this.nannyId = nannyId;
        this.parentId = parentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.age = age;
        this.specialNeeds = specialNeeds;
        this.type = type;
        this.note = note;
        this.status = status;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }

    public void setSpecialNeeds(String specialNeeds) {
        this.specialNeeds = specialNeeds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
