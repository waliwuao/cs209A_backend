package com.example.cs209a_project.dto;

public class SolvabilityComparisonDTO {
    private GroupMetrics highQualityGroup;
    private GroupMetrics hardToSolveGroup;
    private String analysisSummary;

    public SolvabilityComparisonDTO(GroupMetrics highQualityGroup, GroupMetrics hardToSolveGroup, String analysisSummary) {
        this.highQualityGroup = highQualityGroup;
        this.hardToSolveGroup = hardToSolveGroup;
        this.analysisSummary = analysisSummary;
    }

    public GroupMetrics getHighQualityGroup() { return highQualityGroup; }
    public GroupMetrics getHardToSolveGroup() { return hardToSolveGroup; }
    public String getAnalysisSummary() { return analysisSummary; }
}
