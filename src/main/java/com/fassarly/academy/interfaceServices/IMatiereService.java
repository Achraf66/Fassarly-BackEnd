package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.Matiere;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IMatiereService {

    //-----------------------------------CRUD begins-----------------------------------//
    public Matiere createMatiere(Matiere matiere, MultipartFile imageFile) throws IOException;

    public List<Matiere> readAllMatiere();

    public Matiere readMatiere(Long id);

    public Matiere updateMatiere(Matiere matiere);

    public void  deleteMatiere(Long id);
    //-----------------------------------CRUD ends-----------------------------------//

    //------------Rechercher Matiere par nom------------//
    public Matiere findByNomMatiere(String nomMatiere);
}
