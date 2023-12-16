package com.fassarly.academy.services;

import com.fassarly.academy.entities.Abonnement;
import com.fassarly.academy.interfaceServices.IAbonnementService;
import com.fassarly.academy.repositories.AbonnementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AbonnementServiceImpl implements IAbonnementService {

    AbonnementRepository abonnementRepository;

    @Override
    public Abonnement createAbonnement(Abonnement abonnement) {
        return abonnementRepository.save(abonnement);
    }

    @Override
    public List<Abonnement> readAllAbonnement() {
        return abonnementRepository.findAll();
    }

    @Override
    public Abonnement readAbonnement(Long id) {
        return abonnementRepository.findById(id).orElse(null);
    }

    @Override
    public Abonnement updateAbonnement(Abonnement abonnement) {
        return abonnementRepository.save(abonnement);
    }

    @Override
    public void deleteAbonnement(Long id) {
        abonnementRepository.deleteById(id);
    }
}
