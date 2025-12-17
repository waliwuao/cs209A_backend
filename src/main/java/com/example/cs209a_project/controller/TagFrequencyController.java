package com.example.cs209a_project.controller;

import com.example.cs209a_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tag")
public class TagFrequencyController {
    @Autowired
    private TagIdService tagIdService;
    @Autowired
    private TagQuestionService tagQuestionService;
    @Autowired
    private TagFrequencyService tagFrequencyService;
    @Autowired
    private TagCoOccurrenceService tagCoOccurrenceService;
    @Autowired
    private TagQuestionExportService tagQuestionExportService;

    @GetMapping("/id")
    public ResponseEntity<Integer> getIdByTagName(@RequestParam String tagName) {
        try {
            Integer id = tagIdService.getTagId(tagName);
            return ResponseEntity.ok(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/question")
    public ResponseEntity<List<Integer>> getQuestionIdByTagName(@RequestParam String tagName) {
        try {
            List<Integer> questionId = tagQuestionService.getQuestionsId(tagName);
            return ResponseEntity.ok(questionId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/frequency")
    public ResponseEntity<List<Integer>> getFrequencyIdByTagName(@RequestParam String tagName,@RequestParam int scoreParam,@RequestParam int viewCountParam,@RequestParam int answerCountParam) {
        try{
            List<Integer> frequency = tagFrequencyService.getFrequency(tagName,scoreParam,viewCountParam,answerCountParam);
            return ResponseEntity.ok(frequency);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/co-occurrence")
    public ResponseEntity<HashMap<String,Integer>> getCoOccurrenceIdByTagName(@RequestParam String tagName) {
        try{
            HashMap<String,Integer> hashMap = tagCoOccurrenceService.findCoOccurrenceTagByTagName(tagName);
            return ResponseEntity.ok(hashMap);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/data")
    public ResponseEntity<List<Map<String,Object>>> getDataByTagName(@RequestParam String tagName) {
        try{
            List<Map<String,Object>> hashMap = tagQuestionExportService.exportQuestionsWithTagToCsv(tagName);
            return ResponseEntity.ok(hashMap);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }
}
