package com.fassarly.academy.config;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class AzureBlobStorageServiceImpl {
    @Value("${azure.storage.connection-string}")
    private String azureStorageConnectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public String uploadBlob(String blobDirectoryPath, MultipartFile file) {
        try {
            BlobContainerClient containerClient = getBlobContainerClient();
            containerClient.createIfNotExists();

            // Remove leading and trailing slashes from blobDirectoryPath
            blobDirectoryPath = blobDirectoryPath.replaceAll("^/+", "").replaceAll("/+$", "");

            // Ensure that blobDirectoryPath ends with a directory separator
            if (!blobDirectoryPath.isEmpty()) {
                blobDirectoryPath += "/";
            }

            String blobName = blobDirectoryPath + file.getOriginalFilename();
            containerClient.getBlobClient(blobName).upload(file.getInputStream(), file.getSize(), true);

            return blobName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload blob to Azure Blob Storage", e);
        }
    }


    // Add more methods for other Azure Blob Storage operations

    private BlobContainerClient getBlobContainerClient() {
        return new BlobServiceClientBuilder()
                .connectionString(azureStorageConnectionString)
                .buildClient()
                .getBlobContainerClient(containerName);
    }


    public String getBlobUrl(String blobDirectoryPath, String blobName) {
        BlobClient blobClient = getBlobClient(blobDirectoryPath, blobName);

        if (blobClient != null) {
            String encodedUrl = blobClient.getBlobUrl();

            try {
                // Decode the URL
                String decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString());
                return decodedUrl;
            } catch (UnsupportedEncodingException e) {
                // Handle the exception if needed
                e.printStackTrace();
            }
        }

        return null;
    }

    private BlobClient getBlobClient(String blobDirectoryPath, String blobName) {
        try {
            BlobContainerClient containerClient = getBlobContainerClient();
            containerClient.createIfNotExists();

            String fullBlobName = blobDirectoryPath + blobName;
            return containerClient.getBlobClient(fullBlobName);
        } catch (Exception e) {
            // Handle exceptions or log errors
            e.printStackTrace();
            return null;
        }
    }

    public void deleteFolder(String folderPath) {
        BlobContainerClient containerClient = getBlobContainerClient();

        // List all blobs in the container
        Iterable<BlobItem> blobItems = containerClient.listBlobs();

        for (BlobItem blobItem : blobItems) {
            String blobName = blobItem.getName();
            // Check if the blob is inside the specified folder
            if (blobName.startsWith(folderPath)) {
                deleteBlob(blobName);
            }
        }
    }


    private void deleteBlob(String blobName) {
        BlobContainerClient containerClient = getBlobContainerClient();

        try {
            containerClient.getBlobClient(blobName).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
