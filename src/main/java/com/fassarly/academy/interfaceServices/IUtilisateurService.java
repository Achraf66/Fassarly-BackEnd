package com.fassarly.academy.interfaceServices;


import com.fassarly.academy.entities.Utilisateur;

import java.util.List;

public interface IUtilisateurService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Utilisateur createUtilisateur(Utilisateur utilisateur);

    public List<Utilisateur> readAllUtilisateur();

    public Utilisateur readUtilisateur(Long id);

    public Utilisateur updateUtilisateur(Utilisateur utilisateur);

    public void  deleteUtilisateur(Long id);
    //-----------------------------------CRUD ends-----------------------------------//
}
