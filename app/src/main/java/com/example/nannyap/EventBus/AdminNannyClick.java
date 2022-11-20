package com.example.nannyap.EventBus;


import com.example.nannyap.model.NannyModel;

public class AdminNannyClick {

    private boolean success;
    private NannyModel nannyModel;

    public AdminNannyClick(boolean success, NannyModel nannyModel) {
        this.success = success;
        this.nannyModel = nannyModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public NannyModel getNannyModel() {
        return nannyModel;
    }

    public void setNannyModel(NannyModel nannyModel) {
        this.nannyModel = nannyModel;
    }
}
