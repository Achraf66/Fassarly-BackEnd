package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.PrototypeExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrototypeExamRepository extends JpaRepository<PrototypeExam, Long> {

    List<PrototypeExam> findByExamId(Long examId);

}
