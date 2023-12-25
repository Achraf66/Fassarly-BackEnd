package com.fassarly.academy.services;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.interfaceServices.IExamenService;
import com.fassarly.academy.repositories.ExamenRepository;
import com.fassarly.academy.repositories.MatiereRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamenServiceImpl implements IExamenService {

    ExamenRepository examenRepository;

    MatiereRepository matiereRepository;
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
            return examenRepository.findExamenByMatieresId(matiereId);
        } else {
            return Collections.emptyList();
        }
    }



}
