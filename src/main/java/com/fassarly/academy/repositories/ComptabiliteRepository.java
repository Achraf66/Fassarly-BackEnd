package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.entities.Comptabilite;
import com.fassarly.academy.entities.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComptabiliteRepository extends JpaRepository<Comptabilite, Long> {
    boolean existsByAppUserAndMatieres(AppUser appUser, Matiere matiere);
    List<Comptabilite> findByAppUserAndMatieres(AppUser appUser, Matiere matiere);
}
