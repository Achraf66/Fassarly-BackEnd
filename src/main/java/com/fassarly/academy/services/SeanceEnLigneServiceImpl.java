package com.fassarly.academy.services;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.entities.PrototypeExam;
import com.fassarly.academy.entities.SeanceEnLigne;
import com.fassarly.academy.interfaceServices.ISeanceEnLigneService;
import com.fassarly.academy.repositories.MatiereRepository;
import com.fassarly.academy.repositories.SeanceEnLigneRepository;
import com.fassarly.academy.utils.FileUpload;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SeanceEnLigneServiceImpl implements ISeanceEnLigneService {

    SeanceEnLigneRepository seanceEnLigneRepository;

    MatiereRepository matiereRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;


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
        String directoryPath = uploadDirectory+"/seanceEnLigne/" + savedSession.getId() + "/homeWork/";

        if (homeWorkFile != null) {
            String homeWorkFileName = FileUpload.saveFile(directoryPath, homeWorkFile);
            savedSession.setHomeWorkFileName(homeWorkFileName);
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

        // Delete existing homeWorkFile if it exists
        if (homeWorkFile != null) {
            deleteFile(uploadDirectory + "/seanceEnLigne/" + seanceEnLigneId + "/homeWork/" + existingSeanceEnLigne.getHomeWorkFileName());
        }

        // Save new homeWorkFile
        if (homeWorkFile != null && !homeWorkFile.isEmpty()) {
            String homeWorkFileName = FileUpload.saveFile(uploadDirectory + "/seanceEnLigne/" + seanceEnLigneId + "/homeWork/", homeWorkFile);
            existingSeanceEnLigne.setHomeWorkFileName(homeWorkFileName);
        }

        return seanceEnLigneRepository.save(existingSeanceEnLigne);
    }




    @Transactional(rollbackFor = Exception.class)
    public void deleteSeanceEnLigneById(Long liveSessionId) {
        SeanceEnLigne seanceEnLigne = seanceEnLigneRepository.findById(liveSessionId)
                .orElseThrow(() -> new IllegalArgumentException("LiveSession not found with ID: " + liveSessionId));

        // Delete associated folder
        deleteFolder(uploadDirectory+"/seanceEnLigne/" + liveSessionId);

        Matiere matiere = seanceEnLigne.getMatieres();
        // Remove association from parent Examen
        matiere.getSeanceEnLignes().remove(seanceEnLigne);
        matiereRepository.save(matiere);

        // Delete PrototypeExam entity
        seanceEnLigneRepository.delete(seanceEnLigne);
    }




    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFolder(String folderPath) {
        try {
            Path folder = Paths.get(folderPath);
            if (Files.exists(folder)) {
                Files.walk(folder)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            // Handle the exception (e.g., log it)
            e.printStackTrace();
        }
    }

}
