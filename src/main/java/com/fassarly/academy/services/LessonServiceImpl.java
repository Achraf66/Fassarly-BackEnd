package com.fassarly.academy.services;

import com.fassarly.academy.entities.Lesson;
import com.fassarly.academy.entities.Theme;
import com.fassarly.academy.interfaceServices.ILessonService;
import com.fassarly.academy.repositories.LessonRepository;
import com.fassarly.academy.repositories.ThemeRepository;
import com.fassarly.academy.utils.FileUpload;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class LessonServiceImpl implements ILessonService {

    LessonRepository lessonRepository;

    ThemeRepository themeRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

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
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public Lesson createLessonAndAffectToTheme
            (String nomLesson, String videoLien, String description,
             List<MultipartFile> piecesJointes, Long idTheme) throws IOException {
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
            lesson.setVideoLien(videoLien);
            lesson.setThemes(theme);
            themeLessons.add(lesson);

            lessonRepository.save(lesson);

            themeRepository.save(theme);

            // Save the pieceJointes files using the FileUpload service
            List<String> pieceJointesFileNames = new ArrayList<>();
            for (MultipartFile pieceJointe : piecesJointes) {
                String pieceJointeFileName = FileUpload.saveFile(uploadDirectory + "/lessons/" + lesson.getId() + "/piecesJointes/", pieceJointe);
                pieceJointesFileNames.add(pieceJointeFileName);
            }
            lesson.setPiecesJointes(pieceJointesFileNames);

            return lessonRepository.save(lesson);
        }
    }



    @Override
    public Lesson updateLesson(Long lessonId, String nomLesson, String videoLien, String description,
                               List<MultipartFile> piecesJointes) throws IOException {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) {
            return null;
        } else {
            Lesson lesson = optionalLesson.get();

            // Update the lesson properties
            lesson.setNomLesson(nomLesson);
            lesson.setDescription(description);
            lesson.setVideoLien(videoLien);

            // Save the updated lesson
            Lesson updatedLesson = lessonRepository.save(lesson);

            if (piecesJointes != null) {
                // Save the updated pieceJointes files using the FileUpload service
                List<String> pieceJointesFileNames = new ArrayList<>();
                for (MultipartFile pieceJointe : piecesJointes) {
                    String pieceJointeFileName = FileUpload.saveFile(uploadDirectory + "/lessons/" + updatedLesson.getId() + "/piecesJointes/", pieceJointe);
                    pieceJointesFileNames.add(pieceJointeFileName);
                }
                updatedLesson.setPiecesJointes(pieceJointesFileNames);
            }

            return lessonRepository.save(updatedLesson);
        }
    }


    @Override
    public List<Lesson> getLessonsByTheme(Long idTheme) {
        Optional<Theme> optionalTheme = themeRepository.findById(idTheme);
        if (optionalTheme.isEmpty()) {
            return null;
        } else {
            List<Lesson> lessons = lessonRepository.findLessonByThemesId(idTheme);
            lessons.sort(Comparator.comparingLong(Lesson::getId));
            return lessons;
        }

    }


}
