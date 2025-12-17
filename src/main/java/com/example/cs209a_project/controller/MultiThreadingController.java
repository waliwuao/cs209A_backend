package com.example.cs209a_project.controller;

import java.util.List;
import com.example.cs209a_project.service.MultiThreadingService;
import com.example.cs209a_project.model.MultiThreadingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/multi_threads_categories")
@SpringBootApplication
public class MultiThreadingController {
    private final MultiThreadingService classifier;

    @Autowired
    public MultiThreadingController(MultiThreadingService classifier) {
        this.classifier = classifier;
    }
    
    @GetMapping
    public List<MultiThreadingCategory> getCategories() {
        return classifier.getAllCategories();
    }
}