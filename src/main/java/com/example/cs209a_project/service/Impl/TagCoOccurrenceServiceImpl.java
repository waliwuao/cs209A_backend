package com.example.cs209a_project.service.Impl;

import com.example.cs209a_project.model.QuestionTag;
import com.example.cs209a_project.model.Tag;
import com.example.cs209a_project.repository.QuestionTagRepository;
import com.example.cs209a_project.repository.TagRepository;
import com.example.cs209a_project.service.TagCoOccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagCoOccurrenceServiceImpl implements TagCoOccurrenceService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionTagRepository questionTagRepository;

    @Override
    public HashMap<String, Integer> findCoOccurrenceTagByTagName(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("tagName is null or empty");
        }

        Tag targetTag = tagRepository.findByTagName(tagName.trim())
                .orElseThrow(() -> new IllegalArgumentException("Tag not found: " + tagName));
        Integer targetTagId = targetTag.getTagId();

        List<QuestionTag> targetQuestionTags = questionTagRepository.findByIdTagId(targetTagId);
        if (targetQuestionTags.isEmpty()) {
            return new HashMap<>();
        }

        List<Integer> questionIds = targetQuestionTags.stream()
                .map(qt -> qt.getId().getQuestionId())
                .collect(Collectors.toList());

        Map<Integer, Integer> tagIdCountMap = new HashMap<>();
        for (Integer questionId : questionIds) {
            List<QuestionTag> questionTags = questionTagRepository.findByIdQuestionId(questionId);
            for (QuestionTag qt : questionTags) {
                Integer tagId = qt.getId().getTagId();
                if (!tagId.equals(targetTagId) && !tagId.equals(1)) {
                    tagIdCountMap.put(tagId, tagIdCountMap.getOrDefault(tagId, 0) + 1);
                }
            }
        }

        HashMap<String, Integer> resultMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : tagIdCountMap.entrySet()) {
            tagRepository.findById(entry.getKey())
                    .ifPresent(tag -> resultMap.put(tag.getTagName(), entry.getValue()));
        }

        return resultMap;
    }
}
