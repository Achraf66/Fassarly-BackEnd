package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.SeanceEnLigne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeanceEnLigneRepository extends JpaRepository<SeanceEnLigne, Long> {
}
