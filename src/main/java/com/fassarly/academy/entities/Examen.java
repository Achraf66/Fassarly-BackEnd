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
public class Examen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nomExamen;

    //relations
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) //Chercher en chatGpt ?
    Matiere matieres;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    List<PrototypeExam> prototypeExams;




}
