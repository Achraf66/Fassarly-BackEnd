package com.fassarly.academy.entities;

import com.fassarly.academy.token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
public class AppUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nomPrenom;

    @Column(unique = true)
    String numeroTel;

    @JsonIgnore
    String password;

    String photo;

    //relations
    @OneToOne(fetch = FetchType.LAZY)
    Abonnement abonnement;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserRole> roles = new HashSet<>();


    @JsonIgnore
    @OneToMany
    List<Comptabilite> comptabilites;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

}
