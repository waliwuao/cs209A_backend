package com.example.cs209a_project.controller;

import com.example.cs209a_project.dto.SolvabilityComparisonDTO;
import com.example.cs209a_project.service.QuestionAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class QuestionAnalysisController {

    private final QuestionAnalysisService analysisService;

    @Autowired
    public QuestionAnalysisController(QuestionAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/solvability")
    public SolvabilityComparisonDTO getSolvabilityAnalysis() {
        return analysisService.analyzeSolvabilityFactors();
    }
}
