package com.ced.app.model;

public class ResponseJson {
    public String return_message;

    public ResponseJson(String return_message) {
        this.return_message = return_message;
    }

    public ResponseJson() {
    }

    public String getReturn_message() {
        return return_message;
    }

    public void setReturn_message(String return_message) {
        this.return_message = return_message;
    }
}
