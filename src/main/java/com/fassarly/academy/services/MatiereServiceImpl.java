package com.fassarly.academy.services;

import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.interfaceServices.IMatiereService;
import com.fassarly.academy.repositories.MatiereRepository;
import com.fassarly.academy.utils.FileUpload;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class MatiereServiceImpl implements IMatiereService {

    MatiereRepository matiereRepository;

    ImageUploadService imageUploadService;


    @Override
    @Transactional
    public Matiere createMatiere(Matiere matiere, MultipartFile imageFile) throws IOException {

        String uploadDir="C:\\Users\\saifa\\OneDrive\\Desktop\\fassarlyBack\\fassarlyBack\\src\\main\\resources\\uploads";

        String uploadDirMatiere=uploadDir+"\\"+matiere.getNomMatiere();
        // Upload the image and set the path in the matiere object
        String fileName = FileUpload.saveFile(uploadDirMatiere,imageFile);
        matiere.setPhoto(fileName);
        // Save the matiere object to the database
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
        return matiereRepository.save(matiere);
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
}
