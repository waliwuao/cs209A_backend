package com.example.cs209a_project.service;

import java.util.List;
import java.util.Map;

public interface TagQuestionExportService {
    List<Map<String, Object>> exportQuestionsWithTagToCsv(String tagName);
}