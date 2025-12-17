package com.example.cs209a_project.service.Impl;

import com.example.cs209a_project.model.Question;
import com.example.cs209a_project.model.QuestionTag;
import com.example.cs209a_project.repository.QuestionRepository;
import com.example.cs209a_project.repository.QuestionTagRepository;
import com.example.cs209a_project.repository.TagRepository;
import com.example.cs209a_project.service.TagFrequencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagFrequencyServiceImpl implements TagFrequencyService {
    @Autowired
    private QuestionTagRepository questionTagRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagRepository tagRepository;

    private static final YearMonth START_MONTH = YearMonth.of(2023, 12);
    private static final YearMonth END_MONTH = YearMonth.of(2025, 11);
    private static final int TOTAL_MONTHS = 24;
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    private static final List<MonthTimeRange> MONTH_TIME_RANGES = initMonthTimeRanges();

    @Override
    public List<Integer> getFrequency(String tagName,int scoreParam,int viewCountParam,int answerCountParam) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("tagName is null or empty");
        }


        Integer tagId = tagRepository.findByTagName(tagName.trim())
                .map(tag -> tag.getTagId())
                .orElse(null);
        if (tagId == null) {
            return initZeroList();
        }

        List<QuestionTag> questionTagList = questionTagRepository.findByIdTagId(tagId);
        if (questionTagList.isEmpty()) {
            return initZeroList();
        }

        List<Integer> questionIds = questionTagList.stream()
                .map(qt -> qt.getId().getQuestionId())
                .collect(Collectors.toList());

        List<Question> questions = questionRepository.findByQuestionIdIn(questionIds);

        List<Integer> frequencyList = initZeroList();

        for (Question question : questions) {
            try {
                Integer createTimeStamp = question.getCreationDate();
                if (createTimeStamp == null || createTimeStamp == 0) {
                    continue;
                }

                for (int i = 0; i < MONTH_TIME_RANGES.size(); i++) {
                    MonthTimeRange range = MONTH_TIME_RANGES.get(i);
                    if (createTimeStamp >= range.getStartTimestamp() && createTimeStamp < range.getEndTimestamp()) {
                        frequencyList.set(i, frequencyList.get(i) + question.getScore()*scoreParam+question.getViewCount()*viewCountParam+question.getAnswerCount()*answerCountParam);
                        break;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }

        return frequencyList;
    }

    private static List<MonthTimeRange> initMonthTimeRanges() {
        List<MonthTimeRange> ranges = new ArrayList<>(TOTAL_MONTHS);
        YearMonth currentMonth = START_MONTH;
        for (int i = 0; i < TOTAL_MONTHS; i++) {
            LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
            long startTs = startOfMonth.atZone(ZONE_ID).toInstant().getEpochSecond();

            YearMonth nextMonth = currentMonth.plusMonths(1);
            LocalDateTime startOfNextMonth = nextMonth.atDay(1).atStartOfDay();
            long endTs = startOfNextMonth.atZone(ZONE_ID).toInstant().getEpochSecond();

            ranges.add(new MonthTimeRange(startTs, endTs));
            currentMonth = nextMonth;
        }
        return ranges;
    }

    private List<Integer> initZeroList() {
        List<Integer> zeroList = new ArrayList<>(TOTAL_MONTHS);
        for (int i = 0; i < TOTAL_MONTHS; i++) {
            zeroList.add(0);
        }
        return zeroList;
    }

    private static class MonthTimeRange {
        private final long startTimestamp;
        private final long endTimestamp;

        public MonthTimeRange(long startTimestamp, long endTimestamp) {
            this.startTimestamp = startTimestamp;
            this.endTimestamp = endTimestamp;
        }

        public long getStartTimestamp() {
            return startTimestamp;
        }

        public long getEndTimestamp() {
            return endTimestamp;
        }
    }
}