package com.fassarly.academy.interfaceServices;


import com.fassarly.academy.entities.AppUser;

import java.util.List;

public interface IUtilisateurService {
    //-----------------------------------CRUD begins-----------------------------------//
    public AppUser createUtilisateur(AppUser appUser);

    public List<AppUser> readAllUtilisateur();

    public AppUser readUtilisateur(Long id);

    public AppUser updateUtilisateur(AppUser appUser);

    public void  deleteUtilisateur(Long id);
    //-----------------------------------CRUD ends-----------------------------------//
}
