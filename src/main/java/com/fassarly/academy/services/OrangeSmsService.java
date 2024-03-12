package com.fassarly.academy.services;

import com.fassarly.academy.SmsToken.SmsRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@AllArgsConstructor
public class OrangeSmsService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "https://app.tunisiesms.tn/Api/Api.aspx?fct=sms";


    public void sendSms(SmsRequest smsRequest) {
        String url = API_URL +
                "&key=" + smsRequest.getApiKey() +
                "&mobile=" + smsRequest.getMobileNumber() +
                "&sms=" + smsRequest.getSmsContent() +
                "&sender=" + smsRequest.getSender() +
                "&date=" + smsRequest.getDate() +
                "&heure=" + smsRequest.getHeure();

        restTemplate.getForObject(url, String.class);
    }



}
