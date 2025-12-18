package com.example.cs209a_project.controller;

import com.example.cs209a_project.service.MultiThreadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/multi_threads_categories")
public class MultiThreadingController {

    private final MultiThreadingService multiThreadingService;

    @Autowired
    public MultiThreadingController(MultiThreadingService multiThreadingService) {
        this.multiThreadingService = multiThreadingService;
    }

    @GetMapping
    public List<MultiThreadingService.CategoryStats> getCategories() {
        // 调用 Service 中返回 DTO 列表的方法，而不是直接返回枚举
        return multiThreadingService.getCategoryStatistics();
    }
}