package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere , Long> {
    Matiere findByNomMatiere(String nomMatiere);
    List<Matiere> findByComptabilites_AppUser_NumeroTel(String numeroTel);

    @Query("SELECT m FROM Matiere m WHERE LOWER(m.nomMatiere) LIKE %:searchTerm%")
    List<Matiere> searchMatiereByNom(@Param("searchTerm") String searchTerm);

}
