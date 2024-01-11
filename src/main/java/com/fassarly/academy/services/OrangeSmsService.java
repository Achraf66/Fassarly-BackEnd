package com.fassarly.academy.services;

import com.fassarly.academy.SmsToken.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
@AllArgsConstructor
public class OrangeSmsService {

    @Autowired
    private RestTemplate restTemplate;


    @Value("${DEV_PHONE_NUMBER}")
    private  String DEV_PHONE_NUMBER;

    @Value("${TOKEN_ORANGE}")
    private String TOKEN_ORANGE;


    public Map<String, String> sendSms(SmsRequest smsRequest) {
        Map<String, String> response = new HashMap<>();

        try {
            String token = generateToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            String url = "https://api.orange.com/smsmessaging/v1/outbound/tel:+216" + DEV_PHONE_NUMBER + "/requests";

            SmsRequestBody smsRequestBody = new SmsRequestBody();
            smsRequestBody.setOutboundSMSMessageRequest(new OutboundSMSMessageRequest(
                    "tel:+216" + smsRequest.getNumber(),
                    "tel:+216" + DEV_PHONE_NUMBER,
                    new OutboundSMSTextMessage(smsRequest.getMessage())
            ));

            HttpEntity<SmsRequestBody> requestEntity = new HttpEntity<>(smsRequestBody, headers);

            ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

                response.put("successMessage", "SMS sent successfully");

        } catch (Exception e) {
            response.put("error", e.getMessage());
         }
        return response;
    }

    private String generateToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", TOKEN_ORANGE);

            HttpEntity<String> requestEntity = new HttpEntity<>("grant_type=client_credentials", headers);

            ResponseEntity<TokenResponse> responseEntity = new RestTemplate().postForEntity(
                    "https://api.orange.com/oauth/v3/token",
                    requestEntity,
                    TokenResponse.class
            );

            return Objects.requireNonNull(responseEntity.getBody()).getAccess_token();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token: " + e.getMessage());
        }
    }







}
