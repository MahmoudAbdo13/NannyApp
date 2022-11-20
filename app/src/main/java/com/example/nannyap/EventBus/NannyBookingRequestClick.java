package com.example.nannyap.EventBus;


import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;

public class NannyBookingRequestClick {

    private boolean success;
    private BooknigNanny booknigNanny;
    private BookingModel bookingModel;

    public NannyBookingRequestClick(boolean success, BooknigNanny booknigNanny, BookingModel bookingModel) {
        this.success = success;
        this.booknigNanny = booknigNanny;
        this.bookingModel = bookingModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BooknigNanny getBooknigNanny() {
        return booknigNanny;
    }

    public void setBooknigNanny(BooknigNanny booknigNanny) {
        this.booknigNanny = booknigNanny;
    }

    public BookingModel getBookingModel() {
        return bookingModel;
    }

    public void setBookingModel(BookingModel bookingModel) {this.bookingModel = bookingModel;}
}
