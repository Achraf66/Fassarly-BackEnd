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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Comptabilite updateComptabiliteById(Comptabilite comptabilite, Long id) {

  return null;


    }

    @Transactional
    public Map<String, String>  createAndAffectComptabiliteToUser(float paye, float nonPaye, Long idUser, Long idMatiere) {
        // Retrieve the AppUser and Matiere from the repositories
        AppUser appUser = appUserRepository.findById(idUser).orElse(null);
        Matiere matiere = matiereRepository.findById(idMatiere).orElse(null);
        Map<String, String> response = new HashMap<>();

        // Check if the AppUser and Matiere exist
        if (appUser == null || matiere == null) {
            // Handle the case where either the AppUser or Matiere does not exist
            response.put("message", "matiere already Exists for this User");
            return response;

        }
        // Check if the association already exists
        if (comptabiliteRepository.existsByAppUserAndMatieres(appUser, matiere)) {

            response.put("message", "matiere already Exists for this User");
            return response;

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

        response.put("message", "matiere Created and affected to the user");
        return response;
    }
    private boolean matiereIsAlreadyAssociated(AppUser appUser, Matiere matiere) {
        List<Comptabilite> existingAssociations = comptabiliteRepository.findByAppUserAndMatieres(appUser, matiere);
        return !existingAssociations.isEmpty();
    }

    @Transactional
    public Map<String, String> editComptabilite(Long idComptabilite, float newPaye, float newNonPaye, Long newIdMatiere) {
        // Retrieve the existing Comptabilite from the repository
        Comptabilite existingComptabilite = comptabiliteRepository.findById(idComptabilite).orElse(null);
        Map<String, String> response = new HashMap<>();

        // Check if the Comptabilite exists
        if (existingComptabilite == null) {
            response.put("message", "Comptabilite not found");
            return response;
        }

        // Retrieve the new Matiere from the repository
        Matiere newMatiere = matiereRepository.findById(newIdMatiere).orElse(null);

        if (!existingComptabilite.getMatieres().equals(newMatiere) &&
                matiereIsAlreadyAssociated(existingComptabilite.getAppUser(), newMatiere)) {
            response.put("message", "Matiere is already associated with this user");
            return response;
        }


        // Update the existing Comptabilite instance
        existingComptabilite.setPaye(newPaye);
        existingComptabilite.setNonPaye(newNonPaye);
        existingComptabilite.setMatieres(newMatiere);

        // Save the updated Comptabilite instance
        Comptabilite updatedComptabilite = comptabiliteRepository.save(existingComptabilite);

        // Update the associations in the AppUser and Matiere entities (assuming they have a bidirectional relationship)
        AppUser appUser = updatedComptabilite.getAppUser();
        Matiere oldMatiere = updatedComptabilite.getMatieres();

        appUser.getComptabilites().remove(updatedComptabilite);
        oldMatiere.getComptabilites().remove(updatedComptabilite);

        appUser.getComptabilites().add(updatedComptabilite);
        newMatiere.getComptabilites().add(updatedComptabilite);

        // Update the entities in the database
        appUserRepository.save(appUser);
        matiereRepository.save(oldMatiere);
        matiereRepository.save(newMatiere);

        response.put("message", "Comptabilite Updated");
        return response;
    }

}
