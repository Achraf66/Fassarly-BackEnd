package com.fassarly.academy.entities;

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
public class Utilisateur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nomPrenom;

    String numeroTel;

    String mdp;

    String photo;

    //relations
    @OneToOne(fetch = FetchType.LAZY)
    Abonnement abonnement;

    @ManyToMany
    List<Role> roles;

    @OneToMany
    List<Comptabilite> comptabilites;


}
