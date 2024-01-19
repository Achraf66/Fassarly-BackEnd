package com.fassarly.academy.services;

import com.fassarly.academy.config.AzureBlobStorageServiceImpl;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.entities.SeanceEnLigne;
import com.fassarly.academy.interfaceServices.ISeanceEnLigneService;
import com.fassarly.academy.repositories.MatiereRepository;
import com.fassarly.academy.repositories.SeanceEnLigneRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class SeanceEnLigneServiceImpl implements ISeanceEnLigneService {

    SeanceEnLigneRepository seanceEnLigneRepository;

    MatiereRepository matiereRepository;

    @Autowired
    private AzureBlobStorageServiceImpl azureBlobStorageService;



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

    @Transactional
    public SeanceEnLigne createSeanceEnLigneAndAffectToMatiere(Long matiereId,SeanceEnLigne seanceEnLigne, MultipartFile homeWorkFile) throws IOException {
        Matiere matiere = matiereRepository.findById(matiereId).orElse(null);

        if (matiere == null) {
            // Handle the case when the Matiere with given ID is not found
            throw new IllegalArgumentException("Matiere not found for ID: " + matiereId);
        }

        // Set the Matiere for the SeanceEnLigne
        seanceEnLigne.setMatieres(matiere);

        // Save the SeanceEnLigne to get its ID
        SeanceEnLigne savedSession = seanceEnLigneRepository.save(seanceEnLigne);

        // Create the directory path for saving homeWorkFile
        String blobDirectoryPath = "seanceEnLigne/" + savedSession.getId() + "/homeWork/";

        if (homeWorkFile != null) {
            // Upload examFile to Azure Blob Storage
            azureBlobStorageService.uploadBlob(blobDirectoryPath, homeWorkFile);
            seanceEnLigne.setHomeWorkFileName(azureBlobStorageService.getBlobUrl(blobDirectoryPath, homeWorkFile.getOriginalFilename()));
        }

        // Update the list of SeanceEnLigne for the Matiere
        matiere.getSeanceEnLignes().add(savedSession);

        // Save the Matiere to update its SeanceEnLigne list
        matiereRepository.save(matiere);

        return savedSession;
    }



    @Transactional
    public SeanceEnLigne editSeanceEnLigne(Long seanceEnLigneId, SeanceEnLigne updatedSeanceEnLigne, MultipartFile homeWorkFile) throws IOException {
        SeanceEnLigne existingSeanceEnLigne = seanceEnLigneRepository.findById(seanceEnLigneId).orElse(null);

        if (existingSeanceEnLigne == null) {
            throw new IllegalArgumentException("SeanceEnLigne not found for ID: " + seanceEnLigneId);
        }

        // Update properties from the updatedSeanceEnLigne
        existingSeanceEnLigne.setDate(updatedSeanceEnLigne.getDate());
        existingSeanceEnLigne.setHeureDebut(updatedSeanceEnLigne.getHeureDebut());
        existingSeanceEnLigne.setHeureFin(updatedSeanceEnLigne.getHeureFin());
        existingSeanceEnLigne.setTitre(updatedSeanceEnLigne.getTitre());
        existingSeanceEnLigne.setLienZoom(updatedSeanceEnLigne.getLienZoom());

        String blobDirectoryPath = "seanceEnLigne/" + existingSeanceEnLigne.getId() + "/homeWork/";

        // Delete existing homeWorkFile if it exists
//        if (homeWorkFile != null) {
//
//            deleteFile(uploadDirectory + "/seanceEnLigne/" + seanceEnLigneId + "/homeWork/" + existingSeanceEnLigne.getHomeWorkFileName());
//
//        }

        // Save new homeWorkFile
        if (homeWorkFile != null && !homeWorkFile.isEmpty()) {
            azureBlobStorageService.uploadBlob(blobDirectoryPath, homeWorkFile);
            existingSeanceEnLigne.setHomeWorkFileName(azureBlobStorageService.getBlobUrl(blobDirectoryPath, homeWorkFile.getOriginalFilename()));
        }

        return seanceEnLigneRepository.save(existingSeanceEnLigne);
    }




    @Transactional(rollbackFor = Exception.class)
    public void deleteSeanceEnLigneById(Long liveSessionId) {
        SeanceEnLigne seanceEnLigne = seanceEnLigneRepository.findById(liveSessionId)
                .orElseThrow(() -> new IllegalArgumentException("LiveSession not found with ID: " + liveSessionId));

        // Delete associated folder
        String blobDirectoryPath = "seanceEnLigne/" + liveSessionId + "/";

        // Delete associated blob folder from Azure Blob Storage
        azureBlobStorageService.deleteFolder(blobDirectoryPath);


        Matiere matiere = seanceEnLigne.getMatieres();
        // Remove association from parent Examen
        matiere.getSeanceEnLignes().remove(seanceEnLigne);
        matiereRepository.save(matiere);

        // Delete PrototypeExam entity
        seanceEnLigneRepository.delete(seanceEnLigne);
    }



}
