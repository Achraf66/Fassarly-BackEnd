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
public class Lesson implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nomLesson;

    String videoLien;

    @Column(length = 1000)
    String description;

    @ElementCollection
    @CollectionTable(name = "pieces_jointes1", joinColumns = @JoinColumn(name = "id_entite"))
    private List<String> piecesJointes;

    //relations
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    Theme themes;

}
