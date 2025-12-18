package com.example.cs209a_project.service;

import com.example.cs209a_project.model.MultiThreadingCategory;
import com.example.cs209a_project.model.Question;
import com.example.cs209a_project.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MultiThreadingService {
    private final QuestionRepository questionRepository;

    public MultiThreadingService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Map<MultiThreadingCategory, List<QuestionDTO>> classifyAllQuestions() {
        List<Question> allQuestions = questionRepository.findAll();

        // 优化核心：使用 parallelStream() 利用多核 CPU 并行处理
        // Jsoup 解析和正则匹配是 CPU 密集型任务，串行处理非常慢
        return allQuestions.parallelStream()
                .map(question -> {
                    // Jsoup 解析和正则匹配耗时较高
                    MultiThreadingCategory category = MultiThreadingCategory.classify(question.getBody());
                    if (category == null) return null;
                    return new QuestionDTO(
                            question.getQuestionId(),
                            question.getTitle(),
                            category
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(QuestionDTO::getCategory));
    }

    public List<CategoryStats> getCategoryStatistics() {
        Map<MultiThreadingCategory, List<QuestionDTO>> classifiedMap = classifyAllQuestions();

        return Arrays.stream(MultiThreadingCategory.values())
                .map(category -> {
                    long count = classifiedMap.getOrDefault(category, Collections.emptyList()).size();
                    return new CategoryStats(category.name(), count);
                })
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());
    }

    public static class CategoryStats {
        private String category;
        private long count;

        public CategoryStats(String category, long count) {
            this.category = category;
            this.count = count;
        }

        public String getCategory() { return category; }
        public long getCount() { return count; }
    }

    public static class QuestionDTO {
        private int questionId;
        private String title;
        private MultiThreadingCategory category;

        public QuestionDTO(int questionId, String title, MultiThreadingCategory category) {
            this.questionId = questionId;
            this.title = title;
            this.category = category;
        }

        public int getQuestionId() { return questionId; }
        public String getTitle() { return title; }
        public MultiThreadingCategory getCategory() { return category; }
    }
}
