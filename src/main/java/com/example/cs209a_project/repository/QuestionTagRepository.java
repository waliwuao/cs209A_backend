package com.example.cs209a_project.repository;

import com.example.cs209a_project.model.QuestionTag;
import com.example.cs209a_project.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, QuestionTag.QuestionTagId> {
    List<QuestionTag> findByIdTagId(Integer tagId);

    List<QuestionTag> findByIdQuestionId(Integer questionId);
}
