package com.fassarly.academy.interfaceServices;


import com.fassarly.academy.entities.Lesson;

import java.util.List;

public interface ILessonService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Lesson createLesson(Lesson lesson);

    public List<Lesson> readAllLesson();

    public Lesson readLesson(Long id);

    public Lesson updateLesson(Lesson lesson);

    public void  deleteLesson(Long id);
    //-----------------------------------CRUD ends-----------------------------------//
}
