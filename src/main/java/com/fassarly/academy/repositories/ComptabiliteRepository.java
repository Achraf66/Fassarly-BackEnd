package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Comptabilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComptabiliteRepository extends JpaRepository<Comptabilite, Long> {
}
