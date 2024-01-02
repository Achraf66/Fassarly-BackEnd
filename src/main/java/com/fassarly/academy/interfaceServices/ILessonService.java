package com.fassarly.academy.interfaceServices;


import com.fassarly.academy.entities.Lesson;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ILessonService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Lesson createLesson(Lesson lesson);

    public List<Lesson> readAllLesson();

    public Lesson readLesson(Long id);

    public Lesson updateLesson(Lesson lesson);

    public void  deleteLesson(Long id);
    //-----------------------------------CRUD ends-----------------------------------//

    public Lesson createLessonAndAffectToTheme(String nomLesson, String videoLien,String description,List<MultipartFile> piecesJointes,Long idTheme) throws IOException;

    public List<Lesson> getLessonsByTheme(Long idTheme);

    public Lesson updateLesson(Long lessonId, String nomLesson, String videoLien, String description,
                               List<MultipartFile> piecesJointes) throws IOException;
}
