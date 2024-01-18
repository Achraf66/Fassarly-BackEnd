package com.fassarly.academy.controllers;

import com.fassarly.academy.SmsToken.SmsRequest;
import com.fassarly.academy.services.OrangeSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orange")
@CrossOrigin("*")
public class OrangeController {

    @Autowired
    private OrangeSmsService orangeSmsService;

    @PostMapping("/send-sms")
    public ResponseEntity<Map<String, String>> sendSms(@RequestBody SmsRequest smsRequest) {
        Map<String, String> response = orangeSmsService.sendSms(smsRequest);

        if (response.containsKey("successMessage")) {
            return ResponseEntity.ok(response);
        } else if (response.containsKey("errorMessage")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }





}
