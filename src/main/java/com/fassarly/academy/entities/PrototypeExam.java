package com.fassarly.academy.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PrototypeExam implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        String nomPrototypeExam;

        @Column(length = 1000)
        String examFile;

        @Column(length = 1000)
        String correctionFile;

        @Column(length = 1000)
        String correctionLink;

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "examen_id")
        Examen exam;

}
