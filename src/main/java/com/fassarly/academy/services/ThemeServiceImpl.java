package com.fassarly.academy.services;

import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.entities.Theme;
import com.fassarly.academy.interfaceServices.IThemeService;
import com.fassarly.academy.repositories.MatiereRepository;
import com.fassarly.academy.repositories.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ThemeServiceImpl implements IThemeService {

    ThemeRepository themeRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Override
    public Theme createTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    @Override
    public List<Theme> readAllTheme() {
        return themeRepository.findAll();
    }

    @Override
    public Theme readTheme(Long id) {
        return themeRepository.findById(id).orElse(null);
    }

    @Override
    public Theme updateTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    @Override
    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    //------------Rechercher Theme par nom------------//
    @Override
    public Theme findByNomTheme(String nomTheme) {
        return themeRepository.findByNomTheme(nomTheme);
    }

    @Override
    public List<Theme> findThemeByMatieresId(Long matiereId) {
        // Null check for matiereId
        Objects.requireNonNull(matiereId, "Matiere ID cannot be null");

        try {
            List<Theme> themes = themeRepository.findThemeByMatieresId(matiereId);

            // Sorting the themes based on order
            themes.sort(Comparator.comparingLong(Theme::getOrder));

            // Returning an immutable list to prevent modification outside this method
            return Collections.unmodifiableList(themes);
        } catch (Exception ex) {
            // Handle exceptions gracefully
            return Collections.emptyList(); // Or handle the error according to your application's needs
        }
    }


    @Override
    public List<Theme> findThemesSearchInMatiere(String searchTerm,Long matiereId) {
        return themeRepository.findThemesSearchInMatiere(searchTerm,matiereId);
    }

    @Override
    public Theme updateThemeNameById(Integer order, String newThemeName, Long idTheme) {
        Optional<Theme> optionalTheme = themeRepository.findById(idTheme);

        if (optionalTheme.isPresent()) {
            Theme theme = optionalTheme.get();
            theme.setNomTheme(newThemeName);
            theme.setOrder(order);
            return themeRepository.save(theme);
        } else {
            return null;
        }
    }

    @Override
    public Theme findThemeById(Long idTheme) {
        Optional<Theme> optionalTheme = themeRepository.findById(idTheme);
        return optionalTheme.orElse(null);
    }


    @Override
    public Theme addThemeAddaffectToMatiere(Long idMatiere, Theme newTheme) {
        Matiere matiere = matiereRepository.findById(idMatiere).orElse(null);
        newTheme.setMatieres(matiere);
        Theme savedTheme = themeRepository.save(newTheme);
        matiere.getThemes().add(savedTheme);
        matiereRepository.save(matiere);

        return savedTheme;
    }


}
