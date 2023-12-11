package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.SeanceEnLigne;

import java.util.List;

public interface ISeanceEnLigneService {
    //-----------------------------------CRUD begins-----------------------------------//
    public SeanceEnLigne createSeanceEnLigne(SeanceEnLigne seanceEnLigne);

    public List<SeanceEnLigne> readAllSeanceEnLigne();

    public SeanceEnLigne readSeanceEnLigne(Long id);

    public SeanceEnLigne updateSeanceEnLigne(SeanceEnLigne seanceEnLigne);

    public void  deleteSeanceEnLigne(Long id);
    //-----------------------------------CRUD ends-----------------------------------//
}
