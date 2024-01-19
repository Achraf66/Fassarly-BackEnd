package com.fassarly.academy.DTO;

import com.fassarly.academy.entities.Comptabilite;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonSerialize
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDTO {

    private Long id;
    private String nomPrenom;
    private String numeroTel;
    private String photo;
    List<Comptabilite> comptabilites;

}