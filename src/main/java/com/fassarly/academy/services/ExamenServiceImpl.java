package com.fassarly.academy.services;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.interfaceServices.IExamenService;
import com.fassarly.academy.repositories.ExamenRepository;
import com.fassarly.academy.repositories.MatiereRepository;
import com.fassarly.academy.utils.FileUpload;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@AllArgsConstructor
public class ExamenServiceImpl implements IExamenService {

    ExamenRepository examenRepository;

    MatiereRepository matiereRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private final String someString;


    @Override
    public Examen createExamen(Examen examen) {
        return examenRepository.save(examen);
    }

    @Override
    public List<Examen> readAllExamen() {
        return examenRepository.findAll();
    }

    @Override
    public Examen readExamen(Long id) {
        return examenRepository.findById(id).orElse(null);
    }

    @Override
    public Examen updateExamen(Examen examen) {
        return examenRepository.save(examen);
    }

    @Override
    public void deleteExamen(Long id) {
     examenRepository.deleteById(id);
    }

    @Override
    public List<Examen> findExamenByMatieresId(Long matiereId) {
        Optional<Matiere> matiereOptional = matiereRepository.findById(matiereId);

        if (matiereOptional.isPresent()) {
            Matiere matiere = matiereOptional.get();
            List<Examen> examens = examenRepository.findExamenByMatieresId(matiereId);

            // Assuming you have a specific criterion for sorting (e.g., by ID).
            // Replace "Comparator.comparing(Examen::getId)" with your desired criterion.
            Comparator<Examen> comparator = Comparator.comparing(Examen::getId);

            examens.sort(comparator);

            // Make your modifications to the list here.
            // For example, modify an element without changing the order.

            // Return the modified list with the original order.
            return examens;
        } else {
            return Collections.emptyList();
        }
    }



    @Override
    public Examen fetchExamenById(Long idExamen) {
        return examenRepository.findById(idExamen).orElse(null);
    }


    @Override
    public Examen editExamen(Long examenId, String nomExamen) throws IOException {

        Examen examen = examenRepository.findById(examenId).orElse(null);

        if (examen == null) {
            throw new IllegalArgumentException("Examen not found with id: " + examenId);
        }


        // Update examen details
        examen.setNomExamen(nomExamen);


        // Save the updated examen
        examenRepository.save(examen);

        return examen;
    }

    @Transactional
    public Examen createExamenAndAffectToMatiere(Long matiereId, String nomExamen) throws IOException {

        Matiere matiere = matiereRepository.findById(matiereId).orElse(null);

        if (matiere == null) {
            // Handle the case where Matiere is not found, perhaps throw an exception
            throw new IllegalArgumentException("Matiere not found with id: " + matiereId);
        }

        Examen examen = new Examen();
        examen.setNomExamen(nomExamen);
        examenRepository.save(examen);

        examen.setMatieres(matiere);
        examenRepository.save(examen);

        matiere.getExamens().add(examen);
        matiereRepository.save(matiere);

        return examen;
    }



    public List<Examen> searchByPartialNomExamen(String partialNomExamen) {
        return examenRepository.findByPartialNomExamen(partialNomExamen);
    }






}
