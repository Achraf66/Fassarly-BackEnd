package com.fassarly.academy.services;

import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.entities.Comptabilite;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.interfaceServices.IComptabiliteService;
import com.fassarly.academy.repositories.AppUserRepository;
import com.fassarly.academy.repositories.ComptabiliteRepository;
import com.fassarly.academy.repositories.MatiereRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ComptabiliteserviceImpl implements IComptabiliteService {
    ComptabiliteRepository comptabiliteRepository;
    AppUserRepository appUserRepository;
    MatiereRepository matiereRepository;

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
        Optional<Comptabilite> comptabiliteOptional = comptabiliteRepository.findById(id);

        comptabiliteOptional.ifPresent(comptabilite -> {
            comptabilite.setAppUser(null);
            comptabiliteRepository.delete(comptabilite);
        });
    }

    @Transactional
    public String createAndAffectComptabiliteToUser(float paye, float nonPaye, Long idUser, Long idMatiere) {
        // Retrieve the AppUser and Matiere from the repositories
        AppUser appUser = appUserRepository.findById(idUser).orElse(null);
        Matiere matiere = matiereRepository.findById(idMatiere).orElse(null);

        // Check if the AppUser and Matiere exist
        if (appUser == null || matiere == null) {
            // Handle the case where either the AppUser or Matiere does not exist
            return "Matiere already Exists for this User";
        }
        // Check if the association already exists
        if (comptabiliteRepository.existsByAppUserAndMatieres(appUser, matiere)) {

            return "Matiere already Exists for this User";
        }

        // Check if the Matiere is already associated with the AppUser
        if (matiereIsAlreadyAssociated(appUser, matiere)) {
            // Handle the case where the Matiere is already associated with the AppUser
            return null;
        }
        // Create the Comptabilite instance
        Comptabilite comptabilite = Comptabilite.builder()
                .paye(paye)
                .nonPaye(nonPaye)
                .appUser(appUser)
                .matieres(matiere)
                .build();

        // Save the Comptabilite instance
        Comptabilite savedComptabilite = comptabiliteRepository.save(comptabilite);

        // Update the associations in the AppUser and Matiere entities
        appUser.getComptabilites().add(savedComptabilite);
        matiere.getComptabilites().add(savedComptabilite);

        // Update the entities in the database
        appUserRepository.save(appUser);
        matiereRepository.save(matiere);

        return "Matiere Created and affected to the user";
    }
    private boolean matiereIsAlreadyAssociated(AppUser appUser, Matiere matiere) {
        List<Comptabilite> existingAssociations = comptabiliteRepository.findByAppUserAndMatieres(appUser, matiere);
        return !existingAssociations.isEmpty();
    }


}
