package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.Examen;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IExamenService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Examen createExamen(Examen examen);

    public List<Examen> readAllExamen();

    public Examen readExamen(Long id);

    public Examen updateExamen(Examen examen);

    public void  deleteExamen(Long id);
    //-----------------------------------CRUD ends-----------------------------------//

    public List<Examen> findExamenByMatieresId(Long matiereId);


    public Examen createExamenAndAffectToMatiere(Long matiereId, String nomExamen ) throws IOException ;


    public Examen fetchExamenById(Long idExamen);
    public Examen editExamen(Long examenId, String nomExamen) throws IOException;
}
