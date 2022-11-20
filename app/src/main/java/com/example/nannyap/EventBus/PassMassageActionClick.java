package com.example.nannyap.EventBus;


public class PassMassageActionClick {

   private String message;

    public PassMassageActionClick(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
