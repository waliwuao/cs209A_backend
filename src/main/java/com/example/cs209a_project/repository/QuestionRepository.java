package com.example.cs209a_project.repository;

import com.example.cs209a_project.model.Question;
import com.example.cs209a_project.model.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByQuestionIdIn(List<Integer> questionIds);
}
