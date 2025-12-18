package com.example.cs209a_project.dto;

public class GroupMetrics {
    private String groupName;
    private long totalQuestions;
    
    // 1. 基础文本指标
    private double avgTitleLength;
    private double avgBodyLength;
    private double codeSnippetRate;
    
    // 2. 高级分析指标
    private double avgReputation;      // 用户声望
    private double avgTagsPerQuestion; // 话题复杂度
    private double avgLinksPerQuestion;// 研究深度

    public GroupMetrics(String groupName, long totalQuestions, 
                        double avgTitleLength, double avgBodyLength, double codeSnippetRate,
                        double avgReputation, double avgTagsPerQuestion, double avgLinksPerQuestion) {
        this.groupName = groupName;
        this.totalQuestions = totalQuestions;
        this.avgTitleLength = avgTitleLength;
        this.avgBodyLength = avgBodyLength;
        this.codeSnippetRate = codeSnippetRate;
        this.avgReputation = avgReputation;
        this.avgTagsPerQuestion = avgTagsPerQuestion;
        this.avgLinksPerQuestion = avgLinksPerQuestion;
    }

    // Getters
    public String getGroupName() { return groupName; }
    public long getTotalQuestions() { return totalQuestions; }
    public double getAvgTitleLength() { return avgTitleLength; }
    public double getAvgBodyLength() { return avgBodyLength; }
    public double getCodeSnippetRate() { return codeSnippetRate; }
    public double getAvgReputation() { return avgReputation; }
    public double getAvgTagsPerQuestion() { return avgTagsPerQuestion; }
    public double getAvgLinksPerQuestion() { return avgLinksPerQuestion; }
}
