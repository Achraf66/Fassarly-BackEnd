package com.fassarly.academy.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
    public class AuthenticationRequest implements Serializable {

    private String numtel;
    private String password;
}