package com.fassarly.academy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Matiere implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        String nomMatiere;

        @Nullable
        String photo;


    //relations
    @OneToMany(mappedBy = "matieres", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    List<Comptabilite> comptabilites;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "matieres")
    List<Examen> examens;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "matieres")
    List<Theme> themes;
    @JsonIgnore

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "matieres")
    List<SeanceEnLigne> seanceEnLignes;

}
