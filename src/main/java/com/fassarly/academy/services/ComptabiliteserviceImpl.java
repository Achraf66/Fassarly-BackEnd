package com.fassarly.academy.services;

import com.fassarly.academy.entities.Comptabilite;
import com.fassarly.academy.interfaceServices.IComptabiliteService;
import com.fassarly.academy.repositories.ComptabiliteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ComptabiliteserviceImpl implements IComptabiliteService {
    ComptabiliteRepository comptabiliteRepository;

    @Override
    public Comptabilite createComptabilite(Comptabilite comptabilite) {
        return comptabiliteRepository.save(comptabilite);
    }

    @Override
    public List<Comptabilite> readAllComptabilite() {
        return comptabiliteRepository.findAll();
    }

    @Override
    public Comptabilite readComptabilite(Long id) {
        return comptabiliteRepository.findById(id).orElse(null);
    }

    @Override
    public Comptabilite updateComptabilite(Comptabilite comptabilite) {
        return comptabiliteRepository.save(comptabilite);
    }

    @Override
    public void deleteComptabilite(Long id) {
     comptabiliteRepository.deleteById(id);
    }
}
