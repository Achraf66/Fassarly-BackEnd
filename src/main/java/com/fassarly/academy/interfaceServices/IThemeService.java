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

    //-----------------FindThemeByMatiereId(Long MatiereId--------//////
    public List<Theme>findThemeByMatieresId(Long matiereId);

    /*****************************Search Theme By Name******************/
    List<Theme> findThemesSearchInMatiere(String searchTerm,Long matiereId);

    /**************************Update Themename By Id*****************/
    public Theme updateThemeNameById(Integer order , String newThemeName , Long idTheme);

    /******************************Find Theme By Id ********************/
    public Theme findThemeById(Long idTheme);
    /*******************Add theme and AFFECT To matiere****************/
    public Theme addThemeAddaffectToMatiere(Long idMatiere, Theme newTheme);
}
