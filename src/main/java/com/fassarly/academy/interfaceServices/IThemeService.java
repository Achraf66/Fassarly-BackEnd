package com.fassarly.academy.interfaceServices;


import com.fassarly.academy.entities.Theme;

import java.util.List;

public interface IThemeService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Theme createTheme(Theme theme);

    public List<Theme> readAllTheme();

    public Theme readTheme(Long id);

    public Theme updateTheme(Theme theme);

    public void  deleteTheme(Long id);
    //-----------------------------------CRUD ends-----------------------------------//

    //------------Rechercher Theme par nom------------//
    public Theme findByNomTheme(String nomTheme);
}
