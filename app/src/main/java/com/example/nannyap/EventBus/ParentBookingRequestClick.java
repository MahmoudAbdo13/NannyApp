package com.example.nannyap.EventBus;


import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;
import com.example.nannyap.model.BooknigParent;

public class ParentBookingRequestClick {

    private boolean success;
    private BooknigParent booknigParent;
    private BookingModel bookingModel;

    public ParentBookingRequestClick(boolean success, BooknigParent booknigParent, BookingModel bookingModel) {
        this.success = success;
        this.booknigParent = booknigParent;
        this.bookingModel = bookingModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BooknigParent getBooknigParent() {
        return booknigParent;
    }

    public void setBooknigParent(BooknigParent booknigParent) {
        this.booknigParent = booknigParent;
    }

    public BookingModel getBookingModel() {
        return bookingModel;
    }

    public void setBookingModel(BookingModel bookingModel) {this.bookingModel = bookingModel;}
}
