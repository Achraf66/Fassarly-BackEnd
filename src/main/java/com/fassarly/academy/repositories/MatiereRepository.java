package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere , Long> {
    Matiere findByNomMatiere(String nomMatiere);
}
