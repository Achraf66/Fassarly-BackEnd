package com.fassarly.academy.services;


import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.PrototypeExam;
import com.fassarly.academy.repositories.ExamenRepository;
import com.fassarly.academy.repositories.PrototypeExamRepository;
import com.fassarly.academy.utils.FileUpload;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrototypeExamServiceImpl {

    @Autowired
    private PrototypeExamRepository prototypeExamRepository;

    @Autowired
    private ExamenRepository examenRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;


    @Transactional(rollbackFor = Exception.class)
    public PrototypeExam createAndAffectPrototypeExamToExamen(String nomPrototypeExam,
                                                              MultipartFile examFile,
                                                              MultipartFile correctionFile,
                                                              String correctionLink,
                                                              Long examenId) throws IOException {
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new IllegalArgumentException("Examen not found with ID: " + examenId));

        PrototypeExam prototypeExam = PrototypeExam.builder().nomPrototypeExam(nomPrototypeExam).build();
        prototypeExamRepository.save(prototypeExam);

        String directoryPath = uploadDirectory + "/PrototypeExam/" + examen.getId() + "/" + prototypeExam.getId() + "/";
        if(correctionFile!=null) {
            String correctionFileName = FileUpload.saveFile(directoryPath, correctionFile);
            prototypeExam.setCorrectionFile(correctionFileName);
        }
        if(examFile!=null) {
            String examFileName = FileUpload.saveFile(directoryPath, examFile);
            prototypeExam.setExamFile(examFileName);
        }
        prototypeExam.setCorrectionLink(correctionLink);

        List<PrototypeExam> prototypeExams = examen.getPrototypeExams();
        prototypeExams.add(prototypeExam);
        prototypeExam.setExam(examen);

                examenRepository.save(examen);
        return prototypeExamRepository.save(prototypeExam);
    }



    @Transactional(rollbackFor = Exception.class)
    public PrototypeExam editPrototypeExam(Long prototypeExamId,
                                           String nomPrototypeExam,
                                           MultipartFile examFile,
                                           MultipartFile correctionFile,
                                           String correctionLink) throws IOException {
        PrototypeExam prototypeExam = prototypeExamRepository.findById(prototypeExamId)
                .orElseThrow(() -> new IllegalArgumentException("PrototypeExam not found with ID: " + prototypeExamId));

        prototypeExam.setNomPrototypeExam(nomPrototypeExam);
        prototypeExam.setCorrectionLink(correctionLink);

        String directoryPath = uploadDirectory + "/PrototypeExam/" + prototypeExam.getExam().getId() + "/" + prototypeExam.getId() + "/";

        // Delete old exam file
        if (examFile != null && !examFile.isEmpty() && prototypeExam.getExamFile() != null) {
            deleteFile(directoryPath + prototypeExam.getExamFile());
        }

        // Delete old correction file
        if (correctionFile != null && !correctionFile.isEmpty() && prototypeExam.getCorrectionFile() != null) {
            deleteFile(directoryPath + prototypeExam.getCorrectionFile());
        }

        // Save new exam file
        if (examFile != null && !examFile.isEmpty()) {
            String examFileName = FileUpload.saveFile(directoryPath, examFile);
            prototypeExam.setExamFile(examFileName);
        }

        // Save new correction file
        if (correctionFile != null && !correctionFile.isEmpty()) {
            String correctionFileName = FileUpload.saveFile(directoryPath, correctionFile);
            prototypeExam.setCorrectionFile(correctionFileName);
        }

        return prototypeExamRepository.save(prototypeExam);
    }



    @Transactional(rollbackFor = Exception.class)
    public void deletePrototypeExam(Long prototypeExamId) {
        PrototypeExam prototypeExam = prototypeExamRepository.findById(prototypeExamId)
                .orElseThrow(() -> new IllegalArgumentException("PrototypeExam not found with ID: " + prototypeExamId));

        // Delete associated folder
        deleteFolder(uploadDirectory + "/PrototypeExam/" + prototypeExam.getExam().getId() + "/" + prototypeExam.getId() + "/");

        // Remove association from parent Examen
        Examen examen = prototypeExam.getExam();
        examen.getPrototypeExams().remove(prototypeExam);
        examenRepository.save(examen);

        // Delete PrototypeExam entity
        prototypeExamRepository.delete(prototypeExam);
    }

    public List<PrototypeExam> getPrototypeExamsByExamenId(Long examenId) {
        List<PrototypeExam> prototypeExams = prototypeExamRepository.findByExamId(examenId);

        // Assuming you have a specific criterion for sorting (e.g., by ID).
        // Replace "Comparator.comparing(PrototypeExam::getId)" with your desired criterion.
        Comparator<PrototypeExam> comparator = Comparator.comparing(PrototypeExam::getId);

        // Sort the list based on the criterion.
        prototypeExams.sort(comparator);

        // Make your modifications to the list here.
        // For example, modify an element without changing the order.

        // Return the modified list with the original order.
        return prototypeExams;
    }











    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            // Handle the exception (e.g., log it)
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







    public PrototypeExam saveOrUpdatePrototypeExam(PrototypeExam prototypeExam) {
        return prototypeExamRepository.save(prototypeExam);
    }

    public List<PrototypeExam> getAllPrototypeExams() {
        return prototypeExamRepository.findAll();
    }

    public Optional<PrototypeExam> getPrototypeExamById(Long id) {
        return prototypeExamRepository.findById(id);
    }

    public void deletePrototypeExamById(Long id) {
        prototypeExamRepository.deleteById(id);
    }



}
