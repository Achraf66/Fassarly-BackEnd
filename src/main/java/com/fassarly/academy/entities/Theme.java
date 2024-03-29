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
public class Theme implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nomTheme;

    //relations
    @ManyToOne(fetch = FetchType.LAZY)
    Matiere matieres;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "themes")
    List<Lesson> lessons;


}
