package com.example.cs209a_project.repository;

import com.example.cs209a_project.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByTagName(String tagName);

    Optional<Tag> findById(Integer id);
}
