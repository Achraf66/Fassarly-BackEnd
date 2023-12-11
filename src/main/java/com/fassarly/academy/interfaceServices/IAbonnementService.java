package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.Abonnement;

import java.util.List;

public interface IAbonnementService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Abonnement createAbonnement(Abonnement abonnement);

    public List<Abonnement> readAllAbonnement();

    public Abonnement readAbonnement(Long id);

    public Abonnement updateAbonnement(Abonnement abonnement);

    public void  deleteAbonnement(Long id);
    //-----------------------------------CRUD ends-----------------------------------//
}
