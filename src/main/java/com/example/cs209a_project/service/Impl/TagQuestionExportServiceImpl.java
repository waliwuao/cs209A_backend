package com.example.cs209a_project.service.Impl;

import com.example.cs209a_project.model.Question;
import com.example.cs209a_project.model.QuestionTag;
import com.example.cs209a_project.model.Tag;
import com.example.cs209a_project.repository.QuestionRepository;
import com.example.cs209a_project.repository.QuestionTagRepository;
import com.example.cs209a_project.repository.TagRepository;
import com.example.cs209a_project.service.TagQuestionExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagQuestionExportServiceImpl implements TagQuestionExportService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionTagRepository questionTagRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<Map<String, Object>> exportQuestionsWithTagToCsv(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("标签名称不能为空");
        }

        Tag tag = tagRepository.findByTagName(tagName.trim())
                .orElseThrow(() -> new IllegalArgumentException("标签不存在: " + tagName));

        List<QuestionTag> questionTags = questionTagRepository.findByIdTagId(tag.getTagId());
        if (questionTags.isEmpty()) {
            throw new IllegalArgumentException("该标签没有关联的问题");
        }

        List<Integer> questionIds = questionTags.stream()
                .map(qt -> qt.getId().getQuestionId())
                .collect(Collectors.toList());

        List<Question> questions = questionRepository.findByQuestionIdIn(questionIds);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Question question : questions) {
            Map<String, Object> questionMap = new HashMap<>();
            questionMap.put("question_id", question.getQuestionId());
            questionMap.put("title", question.getTitle());
            result.add(questionMap);
        }

        return result;
    }
}