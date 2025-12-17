package com.example.cs209a_project.service.Impl;

import com.example.cs209a_project.model.Tag;
import com.example.cs209a_project.repository.TagRepository;
import com.example.cs209a_project.service.TagIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagIdServiceImpl implements TagIdService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public Integer getTagId(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("tagName is null or empty");
        }

        Tag tag = tagRepository.findByTagName(tagName.trim())
                .orElseThrow(() -> {
                    String errorMsg = "Tag with name " + tagName + " not found";
                    return new IllegalArgumentException(errorMsg);
                });
        return tag.getId();
    }
}