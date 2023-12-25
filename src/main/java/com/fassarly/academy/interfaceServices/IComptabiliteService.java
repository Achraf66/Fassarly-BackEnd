package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.Comptabilite;

import java.util.List;
import java.util.Map;

public interface IComptabiliteService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Comptabilite createComptabilite(Comptabilite comptabilite);

    public List<Comptabilite> readAllComptabilite();

    public Comptabilite readComptabilite(Long id);

    public Comptabilite updateComptabilite(Comptabilite comptabilite);

    public void  deleteComptabilite(Long id);

    public Comptabilite updateComptabiliteById(Comptabilite comptabilite,Long id);

    //-----------------------------------CRUD ends-----------------------------------//

    public Map<String, String> editComptabilite(Long idComptabilite, float newPaye, float newNonPaye, Long newIdMatiere);
}
