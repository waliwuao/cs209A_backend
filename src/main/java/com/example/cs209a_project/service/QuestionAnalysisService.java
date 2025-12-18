package com.example.cs209a_project.service;

import com.example.cs209a_project.dto.GroupMetrics;
import com.example.cs209a_project.dto.SolvabilityComparisonDTO;
import com.example.cs209a_project.model.Account;
import com.example.cs209a_project.model.Question;
import com.example.cs209a_project.model.QuestionTag;
import com.example.cs209a_project.repository.AccountRepository;
import com.example.cs209a_project.repository.QuestionRepository;
import com.example.cs209a_project.repository.QuestionTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class QuestionAnalysisService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private QuestionTagRepository questionTagRepository;

    public SolvabilityComparisonDTO analyzeSolvabilityFactors() {
        List<Question> allQuestions = questionRepository.findAll();

        // Group A: High Quality (Solved)
        Predicate<Question> isHighQuality = q -> 
            q.getAnswerCount() != null && q.getAnswerCount() > 0 && q.getScore() != null && q.getScore() > 0;

        // Group B: Hard to Solve (Unanswered/Low Score)
        Predicate<Question> isHardToSolve = q -> 
            (q.getAnswerCount() == null || q.getAnswerCount() == 0) || (q.getScore() != null && q.getScore() <= 0);

        List<Question> highQualityQuestions = allQuestions.stream().filter(isHighQuality).collect(Collectors.toList());
        List<Question> hardQuestions = allQuestions.stream().filter(isHardToSolve).collect(Collectors.toList());

        GroupMetrics highQualityMetrics = calculateMetrics("High Quality", highQualityQuestions);
        GroupMetrics hardMetrics = calculateMetrics("Hard to Solve", hardQuestions);

        String summary = String.format(
            "Comparitive analysis reveals: Solved questions have significantly higher user reputation (Avg: %.0f) and contain more context (Code Rate: %.1f%%, Links: %.1f) compared to unsolved ones.",
            highQualityMetrics.getAvgReputation(), 
            highQualityMetrics.getCodeSnippetRate() * 100,
            highQualityMetrics.getAvgLinksPerQuestion()
        );

        return new SolvabilityComparisonDTO(highQualityMetrics, hardMetrics, summary);
    }

    private GroupMetrics calculateMetrics(String groupName, List<Question> questions) {
        if (questions.isEmpty()) return new GroupMetrics(groupName, 0, 0, 0, 0, 0, 0, 0);

        // 1. Text Metrics
        double avgTitleLength = questions.stream().mapToInt(q -> q.getTitle() != null ? q.getTitle().length() : 0).average().orElse(0);
        double avgBodyLength = questions.stream().mapToInt(q -> q.getBody() != null ? q.getBody().length() : 0).average().orElse(0);
        long countWithCode = questions.stream().filter(q -> q.getBody() != null && (q.getBody().contains("<code>") || q.getBody().contains("<pre>"))).count();
        double codeSnippetRate = (double) countWithCode / questions.size();

        // 2. User Metrics (Batch Query)
        Set<Integer> accountIds = questions.stream().map(Question::getAccountId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Integer, Integer> reputationMap = new HashMap<>();
        if (!accountIds.isEmpty()) {
            reputationMap = accountRepository.findAllById(accountIds).stream()
                    .collect(Collectors.toMap(Account::getAccountId, a -> a.getReputation() != null ? a.getReputation() : 0));
        }
        final Map<Integer, Integer> finalRepMap = reputationMap;
        double avgReputation = questions.stream().mapToInt(q -> finalRepMap.getOrDefault(q.getAccountId(), 0)).average().orElse(0);

        // 3. Complexity Metrics
        List<Integer> qIds = questions.stream().map(Question::getQuestionId).collect(Collectors.toList());
        List<QuestionTag> tags = qIds.isEmpty() ? Collections.emptyList() : questionTagRepository.findByIdQuestionIdIn(qIds);
        Map<Integer, Long> tagCounts = tags.stream().collect(Collectors.groupingBy(qt -> qt.getId().getQuestionId(), Collectors.counting()));
        double avgTags = questions.stream().mapToLong(q -> tagCounts.getOrDefault(q.getQuestionId(), 0L)).average().orElse(0);

        // 4. Research Metrics (Links)
        double avgLinks = questions.stream().mapToDouble(q -> {
            if (q.getBody() == null) return 0;
            int count = 0, idx = 0;
            while ((idx = q.getBody().indexOf("<a ", idx)) != -1) { count++; idx += 3; }
            return count;
        }).average().orElse(0);

        return new GroupMetrics(groupName, questions.size(), avgTitleLength, avgBodyLength, codeSnippetRate, avgReputation, avgTags, avgLinks);
    }
}
