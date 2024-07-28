package com.fassarly.academy.services;

import com.fassarly.academy.config.AzureBlobStorageServiceImpl;
import com.fassarly.academy.entities.Lesson;
import com.fassarly.academy.entities.Theme;
import com.fassarly.academy.interfaceServices.ILessonService;
import com.fassarly.academy.repositories.LessonRepository;
import com.fassarly.academy.repositories.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor

public class LessonServiceImpl implements ILessonService {

    LessonRepository lessonRepository;

    ThemeRepository themeRepository;

    @Autowired
    private AzureBlobStorageServiceImpl azureBlobStorageService;


    @Override
    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public List<Lesson> readAllLesson() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson readLesson(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public Lesson updateLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    @Transactional
    public void deleteLesson(Long id) {
        String blobDirectoryPath = "lessons/" + id;
        azureBlobStorageService.deleteFolder(blobDirectoryPath);
        lessonRepository.deleteById(id);
    }

    @Override
    public Lesson createLessonAndAffectToTheme(
            String nomLesson,
            String videoLien,
            String description,
            List<MultipartFile> piecesJointes,
            Long idTheme,
            Integer order
            ) throws IOException {
        Optional<Theme> optionalTheme = themeRepository.findById(idTheme);
        if (optionalTheme.isEmpty()) {
            return null;
        } else {
            Theme theme = optionalTheme.get();
            List<Lesson> themeLessons;
            themeLessons = theme.getLessons();

            Lesson lesson = new Lesson();
            lesson.setNomLesson(nomLesson);
            lesson.setDescription(description);
            lesson.setOrder(order);
            lesson.setVideoLien(videoLien);
            lesson.setThemes(theme);
            themeLessons.add(lesson);

            lessonRepository.save(lesson);

            themeRepository.save(theme);

            // Upload lesson-related files to Azure Blob Storage
            String blobDirectoryPath = "lessons/" + lesson.getId() + "/piecesJointes/";

            // Save the pieceJointes files using the AzureBlobStorageService
            List<String> pieceJointesBlobUrls = new ArrayList<>();
            for (MultipartFile pieceJointe : piecesJointes) {
                azureBlobStorageService.uploadBlob(blobDirectoryPath, pieceJointe);
                pieceJointesBlobUrls.add(azureBlobStorageService.getBlobUrl(blobDirectoryPath, pieceJointe.getOriginalFilename()));
            }
            lesson.setPiecesJointes(pieceJointesBlobUrls);

            return lessonRepository.save(lesson);
        }
    }



    @Override
    public Lesson updateLesson
            (Long lessonId,
             String nomLesson,
             String videoLien,
             String description,
             List<MultipartFile> piecesJointes,
             Integer order) throws IOException {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) {
            return null;
        } else {
            Lesson lesson = optionalLesson.get();

            // Update the lesson properties
            lesson.setNomLesson(nomLesson);
            lesson.setDescription(description);
            lesson.setVideoLien(videoLien);
            lesson.setOrder(order);

            // Save the updated lesson
            Lesson updatedLesson = lessonRepository.save(lesson);

            if (piecesJointes != null) {

                // Upload lesson-related files to Azure Blob Storage
                String blobDirectoryPath = "lessons/" + lesson.getId() + "/piecesJointes/";
                azureBlobStorageService.deleteFolder(blobDirectoryPath);

                // Save the pieceJointes files using the AzureBlobStorageService
                List<String> pieceJointesBlobUrls = new ArrayList<>();
                for (MultipartFile pieceJointe : piecesJointes) {
                    azureBlobStorageService.uploadBlob(blobDirectoryPath, pieceJointe);
                    pieceJointesBlobUrls.add(azureBlobStorageService.getBlobUrl(blobDirectoryPath, pieceJointe.getOriginalFilename()));
                }
                lesson.setPiecesJointes(pieceJointesBlobUrls);
            }

            return lessonRepository.save(updatedLesson);
        }
    }


    @Override
    public List<Lesson> getLessonsByTheme(Long idTheme) {
        Objects.requireNonNull(idTheme, "Theme ID cannot be null");
        try {
            Optional<Theme> optionalTheme = themeRepository.findById(idTheme);
            if (optionalTheme.isEmpty()) {
                return Collections.emptyList();
            } else {
                List<Lesson> lessons = lessonRepository.findLessonByThemesId(idTheme);

                lessons.sort(Comparator.comparingLong(Lesson::getOrder));

                return Collections.unmodifiableList(lessons);
            }

        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }



}
