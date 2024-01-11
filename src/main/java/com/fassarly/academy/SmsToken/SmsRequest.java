package com.fassarly.academy.SmsToken;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SmsRequest {

    private String number;
    private String message;


}
