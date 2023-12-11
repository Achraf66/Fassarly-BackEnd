package com.fassarly.academy.services;

import com.fassarly.academy.entities.Utilisateur;
import com.fassarly.academy.interfaceServices.IUtilisateurService;
import com.fassarly.academy.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class UtilisateurServiceImpl implements IUtilisateurService {

    UtilisateurRepository utilisateurRepository;

    @Override
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public List<Utilisateur> readAllUtilisateur() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur readUtilisateur(Long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    @Override
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
