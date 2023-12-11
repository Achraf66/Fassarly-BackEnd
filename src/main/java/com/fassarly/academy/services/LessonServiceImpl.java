package com.fassarly.academy.services;

import com.fassarly.academy.entities.Lesson;
import com.fassarly.academy.interfaceServices.ILessonService;
import com.fassarly.academy.repositories.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor

public class LessonServiceImpl implements ILessonService {

    LessonRepository lessonRepository;

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
}
