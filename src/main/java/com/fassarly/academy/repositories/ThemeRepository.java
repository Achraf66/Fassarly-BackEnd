package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Theme findByNomTheme(String nomTheme);

    List<Theme> findThemeByMatieresId(Long MatiereId);

    @Query("SELECT t FROM Theme t WHERE t.matieres.id = :matiereId AND LOWER(t.nomTheme) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Theme> findThemesSearchInMatiere(@Param("searchTerm") String searchTerm, @Param("matiereId") Long matiereId);


}
