package com.fassarly.academy.DTO;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComptabiliteDTO {

    private Long id;
    private Float paye;
    private Float nonPaye;
    private MatiereDTO matieres;
    private AppUserDTO appUser;

}
