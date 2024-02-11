package com.fassarly.academy.services;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.interfaceServices.IExamenService;
import com.fassarly.academy.repositories.ExamenRepository;
import com.fassarly.academy.repositories.MatiereRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamenServiceImpl implements IExamenService {

    ExamenRepository examenRepository;

    MatiereRepository matiereRepository;

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
            List<Examen> examens = examenRepository.findExamenByMatieresId(matiereId);

            // Assuming you have a specific criterion for sorting (e.g., by ID).
            // Replace "Comparator.comparing(Examen::getId)" with your desired criterion.
            examens.sort(Comparator.comparing(Examen::getOrder));
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
    public Examen editExamen(Long examenId, String nomExamen , Integer orderExamen) throws IOException {

        Examen examen = examenRepository.findById(examenId).orElse(null);

        if (examen == null) {
            throw new IllegalArgumentException("Examen not found with id: " + examenId);
        }


        // Update examen details
        examen.setNomExamen(nomExamen);

        examen.setOrder(orderExamen);

        // Save the updated examen
        examenRepository.save(examen);

        return examen;
    }

    @Transactional
    public Examen createExamenAndAffectToMatiere(Long matiereId, String nomExamen,Integer orderExamen) throws IOException {

        Matiere matiere = matiereRepository.findById(matiereId).orElse(null);

        if (matiere == null) {
            // Handle the case where Matiere is not found, perhaps throw an exception
            throw new IllegalArgumentException("Matiere not found with id: " + matiereId);
        }

        Examen examen = new Examen();
        examen.setNomExamen(nomExamen);
        examen.setOrder(orderExamen);
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
