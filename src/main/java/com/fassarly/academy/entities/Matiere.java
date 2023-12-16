package com.fassarly.academy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Matiere implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nomMatiere;

    String photo;


    //relations
    @JsonIgnore
    @OneToMany
    List<Comptabilite> comptabilites;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "matieres")
    List<Examen> examens;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "matieres")
    List<Theme> themes;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "matieres")
    List<SeanceEnLigne> seanceEnLignes;

}
