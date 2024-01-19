package com.fassarly.academy.services;


import com.fassarly.academy.config.AzureBlobStorageServiceImpl;
import com.fassarly.academy.entities.Examen;
import com.fassarly.academy.entities.PrototypeExam;
import com.fassarly.academy.repositories.ExamenRepository;
import com.fassarly.academy.repositories.PrototypeExamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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

    @Autowired
    private AzureBlobStorageServiceImpl azureBlobStorageService;


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

        // Upload examFile to Azure Blob Storage
        String blobDirectoryPath = "PrototypeExam/" + examen.getId() + "/" + prototypeExam.getId() + "/";

        // Upload examFile to Azure Blob Storage
            azureBlobStorageService.uploadBlob(blobDirectoryPath, examFile);
            prototypeExam.setExamFile(azureBlobStorageService.getBlobUrl(blobDirectoryPath, examFile.getOriginalFilename()));

        // Upload correctionFile to Azure Blob Storage (if present)
            if (correctionFile != null) {
                 azureBlobStorageService.uploadBlob(blobDirectoryPath, correctionFile);
                prototypeExam.setCorrectionFile(azureBlobStorageService.getBlobUrl(blobDirectoryPath, correctionFile.getOriginalFilename()));
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

            Optional<PrototypeExam> existingPrototypeExamOptional = prototypeExamRepository.findById(prototypeExamId);

            if (existingPrototypeExamOptional.isPresent()) {
                PrototypeExam existingPrototypeExam = existingPrototypeExamOptional.get();

                // Update the fields
                existingPrototypeExam.setNomPrototypeExam(nomPrototypeExam);
                existingPrototypeExam.setCorrectionLink(correctionLink);

                // Upload new examFile to Azure Blob Storage if provided
                if (examFile != null) {
                    String blobDirectoryPath = "PrototypeExam/" + existingPrototypeExam.getExam().getId() + "/" + existingPrototypeExam.getId() + "/";
                    azureBlobStorageService.uploadBlob(blobDirectoryPath, examFile);
                    existingPrototypeExam.setExamFile(azureBlobStorageService.getBlobUrl(blobDirectoryPath, examFile.getOriginalFilename()));
                }

                // Upload new correctionFile to Azure Blob Storage if provided
                if (correctionFile != null) {
                    String blobDirectoryPath = "PrototypeExam/" + existingPrototypeExam.getExam().getId() + "/" + existingPrototypeExam.getId() + "/";
                    azureBlobStorageService.uploadBlob(blobDirectoryPath, correctionFile);
                    existingPrototypeExam.setCorrectionFile(azureBlobStorageService.getBlobUrl(blobDirectoryPath, correctionFile.getOriginalFilename()));
                }

                // Save the updated prototype exam object to the database
                return prototypeExamRepository.save(existingPrototypeExam);
            } else {
                throw new EntityNotFoundException("PrototypeExam with ID " + prototypeExamId + " not found");
            }
        }




    @Transactional(rollbackFor = Exception.class)
    public void deletePrototypeExam(Long prototypeExamId) {
        PrototypeExam prototypeExam = prototypeExamRepository.findById(prototypeExamId)
                .orElseThrow(() -> new IllegalArgumentException("PrototypeExam not found with ID: " + prototypeExamId));

        // Delete associated blob folder from Azure Blob Storage
        String blobDirectoryPath = "PrototypeExam/" + prototypeExam.getExam().getId() +"/"+prototypeExamId;
        azureBlobStorageService.deleteFolder(blobDirectoryPath);

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
