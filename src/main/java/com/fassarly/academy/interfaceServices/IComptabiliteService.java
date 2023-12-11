package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.Comptabilite;

import java.util.List;

public interface IComptabiliteService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Comptabilite createComptabilite(Comptabilite comptabilite);

    public List<Comptabilite> readAllComptabilite();

    public Comptabilite readComptabilite(Long id);

    public Comptabilite updateComptabilite(Comptabilite comptabilite);

    public void  deleteComptabilite(Long id);
    //-----------------------------------CRUD ends-----------------------------------//
}
