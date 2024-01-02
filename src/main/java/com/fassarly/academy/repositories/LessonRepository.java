package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {


    List<Lesson> findLessonByThemesId(Long idTheme);
}
