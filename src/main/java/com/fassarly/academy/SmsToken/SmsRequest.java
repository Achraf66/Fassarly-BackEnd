package com.fassarly.academy.SmsToken;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SmsRequest {
    private String apiKey;
    private String mobileNumber;
    private String smsContent;
    private String sender;
    private String date;
    private String heure;
}
