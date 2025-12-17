package com.example.cs209a_project.service;

import java.util.HashMap;

public interface TagCoOccurrenceService {
    HashMap<String,Integer> findCoOccurrenceTagByTagName(String tagName);
}
