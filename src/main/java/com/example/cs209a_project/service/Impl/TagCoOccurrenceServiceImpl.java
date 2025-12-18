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

        // 1. 获取目标 Tag ID
        Tag targetTag = tagRepository.findByTagName(tagName.trim())
                .orElseThrow(() -> new IllegalArgumentException("Tag not found: " + tagName));
        Integer targetTagId = targetTag.getTagId();

        // 2. 获取该 Tag 关联的所有问题 ID
        List<QuestionTag> targetQuestionTags = questionTagRepository.findByIdTagId(targetTagId);
        if (targetQuestionTags.isEmpty()) {
            return new HashMap<>();
        }

        List<Integer> questionIds = targetQuestionTags.stream()
                .map(qt -> qt.getId().getQuestionId())
                .collect(Collectors.toList());

        // 3. 优化核心：批量获取这些问题下的所有 Tag，代替循环查询
        // 以前是循环几千次查询数据库，现在只查一次
        List<QuestionTag> allRelatedTags = questionTagRepository.findByIdQuestionIdIn(questionIds);

        // 4. 在内存中进行聚合统计，速度极快
        Map<Integer, Integer> tagIdCountMap = new HashMap<>();
        for (QuestionTag qt : allRelatedTags) {
            Integer tid = qt.getId().getTagId();
            // 排除自身和一些无意义的 Tag (如 id=1 假设是 java)
            if (!tid.equals(targetTagId) && !tid.equals(1)) {
                tagIdCountMap.put(tid, tagIdCountMap.getOrDefault(tid, 0) + 1);
            }
        }

        // 5. 转换 ID 为 Name (这里会有 N 次查询，但 Tag 总数通常不多且有缓存，也可以优化为批量查)
        HashMap<String, Integer> resultMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : tagIdCountMap.entrySet()) {
            tagRepository.findById(entry.getKey())
                    .ifPresent(tag -> resultMap.put(tag.getTagName(), entry.getValue()));
        }

        return resultMap;
    }
}
