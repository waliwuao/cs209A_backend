package com.example.cs209a_project.service.Impl;

import com.example.cs209a_project.model.QuestionTag;
import com.example.cs209a_project.repository.QuestionTagRepository;
import com.example.cs209a_project.repository.TagRepository;
import com.example.cs209a_project.service.TagQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagQuestionServiceImpl implements TagQuestionService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionTagRepository questionTagRepository;

    @Override
    public List<Integer> getQuestionsId(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("tagName is null or empty");
        }

        return tagRepository.findByTagName(tagName.trim())
                .map(tag -> {
                    List<QuestionTag> questionTags = questionTagRepository.findByIdTagId(tag.getTagId());
                    return questionTags.stream()
                            .map(qt -> qt.getId().getQuestionId())
                            .collect(Collectors.toList());
                })
                .orElseThrow(() -> new IllegalArgumentException("tag not exist: " + tagName));
    }
}