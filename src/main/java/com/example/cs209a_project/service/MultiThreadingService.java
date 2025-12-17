package com.example.cs209a_project.service;

import com.example.cs209a_project.repository.QuestionRepository;
import com.example.cs209a_project.model.MultiThreadingCategory;
import com.example.cs209a_project.model.Question;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service   //define business logic
public class MultiThreadingService {
    private final QuestionRepository questionRepository;

    public MultiThreadingService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Map<MultiThreadingCategory, List<QuestionDTO>> classifyAllQuestions() {
        List<Question> allQuestions = questionRepository.findAll();
        return allQuestions.stream()
                .filter(question -> MultiThreadingCategory.classify(question.getBody()) != null)   //都是多线程相关的类
                .map(question -> {
                    MultiThreadingCategory category = MultiThreadingCategory.classify(question.getBody());   //每个问题分类
                    return new QuestionDTO(
                            question.getQuestionId(),
                            question.getTitle(),
                            category
                    );
                })
                .collect(Collectors.groupingBy(QuestionDTO::getCategory));
    }

    public List<MultiThreadingCategory> getAllCategories() {
        Map<MultiThreadingCategory, List<QuestionDTO>> classifiedMap = classifyAllQuestions();
        for (Map.Entry<MultiThreadingCategory, List<QuestionDTO>> entry : classifiedMap.entrySet()) {
            MultiThreadingCategory category = entry.getKey();
            long count = entry.getValue().size();
            category.setQuestionCount(count);
        }
        return classifiedMap.keySet().stream().toList();
    }

    public Map<String, Long> countCategoryDistribution() {     //便于画图
        Map<MultiThreadingCategory, List<QuestionDTO>> classifiedMap = classifyAllQuestions();
        return classifiedMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> (long) entry.getValue().size()
                ));
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

        public MultiThreadingCategory getCategory() {
            return category;
        }
    }
}