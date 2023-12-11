package com.fassarly.academy.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comptabilite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Float paye;

    Float nonPaye;


    //relations
    @ManyToOne
    @JoinColumn(name = "utilisateur_id") //Foreign Key
    Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id") //Foreign Key
    Matiere matieres;
}
