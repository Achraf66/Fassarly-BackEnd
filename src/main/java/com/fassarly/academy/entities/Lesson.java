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
public class Lesson implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nomLesson;

    String videoLien;

    @ElementCollection
    @CollectionTable(name = "pieces_jointes1", joinColumns = @JoinColumn(name = "id_entite"))
    private List<String> piecesJointes;

    //relations
    @ManyToOne(fetch = FetchType.LAZY)
    Theme themes;

}
