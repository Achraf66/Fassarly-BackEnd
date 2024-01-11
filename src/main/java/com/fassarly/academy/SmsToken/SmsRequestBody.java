package com.fassarly.academy.SmsToken;

public class SmsRequestBody {

    private OutboundSMSMessageRequest outboundSMSMessageRequest;

    public SmsRequestBody() {
        // Default constructor required for Jackson
    }

    public OutboundSMSMessageRequest getOutboundSMSMessageRequest() {
        return outboundSMSMessageRequest;
    }

    public void setOutboundSMSMessageRequest(OutboundSMSMessageRequest outboundSMSMessageRequest) {
        this.outboundSMSMessageRequest = outboundSMSMessageRequest;
    }
}
