package com.fassarly.academy.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.fassarly.academy.entities.Matiere;
import com.fassarly.academy.interfaceServices.IMatiereService;
import com.fassarly.academy.repositories.AppUserRepository;
import com.fassarly.academy.repositories.MatiereRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MatiereServiceImpl implements IMatiereService {

    MatiereRepository matiereRepository;

    ImageUploadService imageUploadService;

    AppUserRepository appUserRepository;

    @Value("${azure.storage.connection-string}")
    private String azureStorageConnectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;


    @Override
    @Transactional
    public Matiere createMatiere(String nomMatiere, MultipartFile imageFile) throws IOException {

     Matiere matiere = new Matiere();
     matiere.setNomMatiere(nomMatiere);

     // Save the matiere object to the database to get the ID
     matiere = matiereRepository.save(matiere);

        // Initialize BlobServiceClient
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureStorageConnectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        containerClient.createIfNotExists();

        // Upload the image to Azure Blob Storage
        String blobName = "matieres/" + matiere.getId() + "/" + imageFile.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        try (InputStream imageStream = imageFile.getInputStream()) {
            blobClient.upload(imageStream, imageFile.getSize());
        }

      matiere.setNomMatiere(nomMatiere);
      matiere.setPhoto(blobClient.getBlobUrl().toString()); // Use the Blob URL as the photo path

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
        Optional<Matiere> existingMatiereOptional = matiereRepository.findById(matiereId);

        if (existingMatiereOptional.isPresent()) {
            Matiere existingMatiere = existingMatiereOptional.get();

            // Update the fields other than the image
            existingMatiere.setNomMatiere(nomMatiere);
            // Update other fields if needed

            if (imageFile != null) {
                // If the file is not null, update the image

                // Initialize BlobServiceClient
                BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                        .connectionString(azureStorageConnectionString)
                        .buildClient();
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                containerClient.createIfNotExists();

                // Upload the image to Azure Blob Storage
                String blobName = "matieres/" + existingMatiere.getId() + "/" + imageFile.getOriginalFilename();
                BlobClient blobClient = containerClient.getBlobClient(blobName);

                try (InputStream imageStream = imageFile.getInputStream()) {
                    blobClient.upload(imageStream, imageFile.getSize());
                }

                existingMatiere.setPhoto(blobClient.getBlobUrl().toString()); // Use the Blob URL as the photo path
            }

            // Save the updated matiere object to the database
            return matiereRepository.save(existingMatiere);
        } else {
            throw new EntityNotFoundException("Matiere with ID " + matiereId + " not found");
        }
    }




    public List<Matiere> searchMatiereByNom(String searchTerm) {
        return matiereRepository.searchMatiereByNom(searchTerm.toLowerCase());
    }









    }
