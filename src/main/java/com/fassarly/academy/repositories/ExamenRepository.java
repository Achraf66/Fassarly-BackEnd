package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {


    List<Examen> findExamenByMatieresId(Long matiereId);

    @Query("SELECT e FROM Examen e WHERE LOWER(e.nomExamen) LIKE LOWER(CONCAT('%', :nomExamen, '%'))")
    List<Examen> findByPartialNomExamen(@Param("nomExamen") String nomExamen);

}
