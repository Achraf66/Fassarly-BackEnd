package com.fassarly.academy.SmsToken;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifySmsRequest {
    private String phoneNumber;
    private String verificationCode;
}