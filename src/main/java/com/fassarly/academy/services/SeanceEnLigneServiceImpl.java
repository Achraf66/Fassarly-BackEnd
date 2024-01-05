package com.fassarly.academy.services;

import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.entities.SeanceEnLigne;
import com.fassarly.academy.interfaceServices.ISeanceEnLigneService;
import com.fassarly.academy.repositories.MatiereRepository;
import com.fassarly.academy.repositories.SeanceEnLigneRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SeanceEnLigneServiceImpl implements ISeanceEnLigneService {

    SeanceEnLigneRepository seanceEnLigneRepository;

    MatiereRepository matiereRepository;

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

    @Override
    public List<SeanceEnLigne> getSessionLiveByMatiereId(Long matiereId) {
        Assert.notNull(matiereId, "MatiereId must not be null");

        Matiere matiere = matiereRepository.findById(matiereId)
                .orElse(null);

        List<SeanceEnLigne> seanceEnLignes = seanceEnLigneRepository.findByMatieres(matiere);

        return seanceEnLignes != null ? seanceEnLignes : Collections.emptyList();
    }

    public SeanceEnLigne createSeanceEnLigneAndAffectToMatiere(Long matiereId, SeanceEnLigne seanceEnLigne) {

            Matiere matiere = matiereRepository.findById(matiereId).orElse(null);

            seanceEnLigne.setMatieres(matiere);

            assert matiere != null;
            List<SeanceEnLigne> seanceEnLignes=matiere.getSeanceEnLignes();
            seanceEnLignes.add(seanceEnLigne);

               matiereRepository.save(matiere);
        return seanceEnLigneRepository.save(seanceEnLigne);
    }


    @Transactional
    public SeanceEnLigne editSeanceEnLigne(Long seanceEnLigneId, SeanceEnLigne seanceEnLigne) {
        // Check if the SeanceEnLigne with the given ID exists
        Optional<SeanceEnLigne> optionalSeanceEnLigne = seanceEnLigneRepository.findById(seanceEnLigneId);

        if (optionalSeanceEnLigne.isPresent()) {
            // Get the existing SeanceEnLigne
            SeanceEnLigne seanceEnLigneExist = optionalSeanceEnLigne.get();

            // Update properties of the existing SeanceEnLigne with the new values
            seanceEnLigneExist.setDate(seanceEnLigne.getDate());
            seanceEnLigneExist.setHeureDebut(seanceEnLigne.getHeureDebut());
            seanceEnLigneExist.setHeureFin(seanceEnLigne.getHeureFin());
            seanceEnLigneExist.setLienZoom(seanceEnLigne.getLienZoom());
            seanceEnLigneExist.setTitre(seanceEnLigne.getTitre());

            // ... continue with other properties

            // Save the updated SeanceEnLigne
            return seanceEnLigneRepository.save(seanceEnLigneExist);
        } else {
            return null;
        }
    }

}
