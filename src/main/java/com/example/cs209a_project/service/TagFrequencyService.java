package com.example.cs209a_project.service;

import java.util.List;

public interface TagFrequencyService {
    List<Integer> getFrequency(String tagName,int scoreParam,int viewCountParam,int answerCountParam);
}
