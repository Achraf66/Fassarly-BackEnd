package com.fassarly.academy.SmsToken;

public class OutboundSMSTextMessage {

    private String message;

    public OutboundSMSTextMessage() {
        // Default constructor required for Jackson
    }

    public OutboundSMSTextMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
