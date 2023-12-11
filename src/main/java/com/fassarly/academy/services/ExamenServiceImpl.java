package com.fassarly.academy.services;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.interfaceServices.IExamenService;
import com.fassarly.academy.repositories.ExamenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ExamenServiceImpl implements IExamenService {

    ExamenRepository examenRepository;

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
}
