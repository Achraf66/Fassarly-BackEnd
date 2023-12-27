package com.fassarly.academy.services;

import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.interfaceServices.IExamenService;
import com.fassarly.academy.repositories.ExamenRepository;
import com.fassarly.academy.repositories.MatiereRepository;
import com.fassarly.academy.utils.FileUpload;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamenServiceImpl implements IExamenService {

    ExamenRepository examenRepository;

    MatiereRepository matiereRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private final String someString;


    @Override
    public Examen createExamen(Examen examen) {
        return examenRepository.save(examen);
    }

    @Override
    public List<Examen> readAllExamen() {
        return examenRepository.findAll();
    }

    @Override
    public Examen readExamen(Long id) {
        return examenRepository.findById(id).orElse(null);
    }

    @Override
    public Examen updateExamen(Examen examen) {
        return examenRepository.save(examen);
    }

    @Override
    public void deleteExamen(Long id) {
     examenRepository.deleteById(id);
    }

    @Override
    public List<Examen> findExamenByMatieresId(Long matiereId) {
        Optional<Matiere> matiereOptional = matiereRepository.findById(matiereId);

        if (matiereOptional.isPresent()) {
            Matiere matiere = matiereOptional.get();
            return examenRepository.findExamenByMatieresId(matiereId);
        } else {
            return Collections.emptyList();
        }
    }


    @Transactional
    public Examen createExamenAndAffectToMatiere(Long matiereId, String nomExamen,
                                                 String videoLien,
                                                 MultipartFile correctionFile,
                                                 List<MultipartFile> pieceJointes) throws IOException {

        Matiere matiere = matiereRepository.findById(matiereId).orElse(null);

        if (matiere == null) {
            // Handle the case where Matiere is not found, perhaps throw an exception
            throw new IllegalArgumentException("Matiere not found with id: " + matiereId);
        }

        Examen examen = new Examen();
        examen.setNomExamen(nomExamen);
        examen.setVideoLien(videoLien);

        // Set other properties of the examen

        // Save the correction file using the FileUpload service
        String correctionFileName = FileUpload.saveFile(uploadDirectory + "\\" + "Exams\\"  + matiereId + nomExamen  + "\\", correctionFile);
        examen.setCorrection(correctionFileName);

        // Save the pieceJointes files using the FileUpload service
        List<String> pieceJointesFileNames = new ArrayList<>();
        for (MultipartFile pieceJointe : pieceJointes) {
            String pieceJointeFileName = FileUpload.saveFile(uploadDirectory + "\\" + "Exams\\" + matiereId + nomExamen  + "\\piecesJointes\\", pieceJointe);
            pieceJointesFileNames.add(pieceJointeFileName);
        }
        examen.setPieceJointes(pieceJointesFileNames);
        examen.setMatieres(matiere);
        examenRepository.save(examen);

        matiere.getExamens().add(examen);
        matiereRepository.save(matiere);

        return examen;
    }




}
