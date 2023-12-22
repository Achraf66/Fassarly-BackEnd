package com.fassarly.academy.services;

import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.interfaceServices.IMatiereService;
import com.fassarly.academy.repositories.AppUserRepository;
import com.fassarly.academy.repositories.MatiereRepository;
import com.fassarly.academy.utils.FileUpload;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MatiereServiceImpl implements IMatiereService {

    MatiereRepository matiereRepository;

    ImageUploadService imageUploadService;

    AppUserRepository appUserRepository;


    @Override
    @Transactional
    public Matiere createMatiere(String nomMatiere, MultipartFile imageFile) throws IOException {

        String uploadDir = "C:\\Users\\saifa\\OneDrive\\Desktop\\fassarlyBack\\fassarlyBack\\src\\main\\resources\\uploads";

        // Create matieres folder if not exists
        String matieresDir = uploadDir + "\\matieres";
        File matieresFolder = new File(matieresDir);
        if (!matieresFolder.exists()) {
            matieresFolder.mkdirs();
        }

        Matiere matiere = new Matiere();
        matiere.setNomMatiere(nomMatiere);

        // Save the matiere object to the database to get the ID
        matiere = matiereRepository.save(matiere);

        // Create a folder for the matiere based on its ID
        String uploadDirMatiere = matieresDir + "\\" + matiere.getId();
        File matiereFolder = new File(uploadDirMatiere);
        if (!matiereFolder.exists()) {
            matiereFolder.mkdirs();
        }

        // Upload the image and set the path in the matiere object
        String fileName = FileUpload.saveFile(uploadDirMatiere, imageFile);
        matiere.setPhoto(fileName);

        // Save the updated matiere object to the database
        return matiereRepository.save(matiere);
    }


    @Override
    public List<Matiere> readAllMatiere() {
        return matiereRepository.findAll();
    }

    @Override
    public Matiere readMatiere(Long id) {
        return matiereRepository.findById(id).orElse(null);
    }

    @Override
    public Matiere updateMatiere(Matiere matiere) {
        return null;
    }

    @Override
    public void deleteMatiere(Long id) {
        matiereRepository.deleteById(id);
    }

    //------------Rechercher Matiere par nom------------//
    @Override
    public Matiere findByNomMatiere(String nomMatiere) {
        return matiereRepository.findByNomMatiere(nomMatiere);
    }


    public List<Matiere> findMatiereByUser(String numtel){
    return matiereRepository.findByComptabilites_AppUser_NumeroTel(numtel);
    }

    @Transactional
    public Matiere updateMatiere(Long matiereId, String nomMatiere, MultipartFile imageFile) throws IOException {
        String uploadDir = "C:\\Users\\saifa\\OneDrive\\Desktop\\fassarlyBack\\fassarlyBack\\src\\main\\resources\\uploads";

        Optional<Matiere> existingMatiereOptional = matiereRepository.findById(matiereId);

        if (existingMatiereOptional.isPresent()) {
            Matiere existingMatiere = existingMatiereOptional.get();

            // Update the fields other than the image
            existingMatiere.setNomMatiere(nomMatiere);
            // Update other fields if needed

            // Create matieres folder if not exists
            String matieresDir = uploadDir + "\\matieres";
            File matieresFolder = new File(matieresDir);
            if (!matieresFolder.exists()) {
                matieresFolder.mkdirs();
            }

            // Create a folder for the matiere based on its ID
            String uploadDirMatiere = matieresDir + "\\" + existingMatiere.getId();
            File matiereFolder = new File(uploadDirMatiere);
            if (!matiereFolder.exists()) {
                matiereFolder.mkdirs();
            }

            if (imageFile != null) {
                // If the file is not null, update the image
                String fileName = FileUpload.saveFile(uploadDirMatiere, imageFile);
                existingMatiere.setPhoto(fileName);
            }

            // Save the updated matiere object to the database
            return matiereRepository.save(existingMatiere);
        } else {
            throw new EntityNotFoundException("Matiere with ID " + matiereId + " not found");
        }
    }













    }
