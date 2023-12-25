package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {


    List<Examen> findExamenByMatieresId(Long matiereId);
}
