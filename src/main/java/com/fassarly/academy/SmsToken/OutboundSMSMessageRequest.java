package com.fassarly.academy.SmsToken;

public class OutboundSMSMessageRequest {

    private String address;
    private String senderAddress;
    private OutboundSMSTextMessage outboundSMSTextMessage;

    public OutboundSMSMessageRequest() {
        // Default constructor required for Jackson
    }

    public OutboundSMSMessageRequest(String address, String senderAddress, OutboundSMSTextMessage outboundSMSTextMessage) {
        this.address = address;
        this.senderAddress = senderAddress;
        this.outboundSMSTextMessage = outboundSMSTextMessage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public OutboundSMSTextMessage getOutboundSMSTextMessage() {
        return outboundSMSTextMessage;
    }

    public void setOutboundSMSTextMessage(OutboundSMSTextMessage outboundSMSTextMessage) {
        this.outboundSMSTextMessage = outboundSMSTextMessage;
    }
}
