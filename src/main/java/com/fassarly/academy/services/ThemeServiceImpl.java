package com.fassarly.academy.services;

import com.fassarly.academy.entities.Theme;
import com.fassarly.academy.interfaceServices.IThemeService;
import com.fassarly.academy.repositories.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ThemeServiceImpl implements IThemeService {

    ThemeRepository themeRepository;
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
}
