package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.Examen;

import java.util.List;

public interface IExamenService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Examen createExamen(Examen examen);

    public List<Examen> readAllExamen();

    public Examen readExamen(Long id);

    public Examen updateExamen(Examen examen);

    public void  deleteExamen(Long id);
    //-----------------------------------CRUD ends-----------------------------------//
}
