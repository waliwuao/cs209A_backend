package com.example.cs209a_project.repository;

import com.example.cs209a_project.model.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, QuestionTag.QuestionTagId> {
    List<QuestionTag> findByIdTagId(Integer tagId);

    List<QuestionTag> findByIdQuestionId(Integer questionId);

    // 优化新增：批量查询接口，解决 N+1 问题
    List<QuestionTag> findByIdQuestionIdIn(List<Integer> questionIds);
}
