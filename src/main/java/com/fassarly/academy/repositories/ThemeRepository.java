package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Theme findByNomTheme(String nomTheme);
}
