package com.fassarly.academy.services;

import com.fassarly.academy.entities.SeanceEnLigne;
import com.fassarly.academy.interfaceServices.ISeanceEnLigneService;
import com.fassarly.academy.repositories.SeanceEnLigneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SeanceEnLigneServiceImpl implements ISeanceEnLigneService {
    SeanceEnLigneRepository seanceEnLigneRepository;
    @Override
    public SeanceEnLigne createSeanceEnLigne(SeanceEnLigne seanceEnLigne) {
        return seanceEnLigneRepository.save(seanceEnLigne);
    }

    @Override
    public List<SeanceEnLigne> readAllSeanceEnLigne() {
        return seanceEnLigneRepository.findAll();
    }

    @Override
    public SeanceEnLigne readSeanceEnLigne(Long id) {
        return seanceEnLigneRepository.findById(id).orElse(null);
    }

    @Override
    public SeanceEnLigne updateSeanceEnLigne(SeanceEnLigne seanceEnLigne) {
        return seanceEnLigneRepository.save(seanceEnLigne);
    }

    @Override
    public void deleteSeanceEnLigne(Long id) {
        seanceEnLigneRepository.deleteById(id);
    }
}
