package com.example.cs209a_project.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "question_tag")
public class QuestionTag {
    @EmbeddedId
    private QuestionTagId id;

    public QuestionTag() {}

    public QuestionTag(QuestionTagId id) {
        this.id = id;
    }

    public QuestionTagId getId() {
        return id;
    }

    public void setId(QuestionTagId id) {
        this.id = id;
    }

    @Embeddable
    public static class QuestionTagId implements Serializable {
        private Integer questionId;
        private Integer tagId;

        public QuestionTagId() {}

        public QuestionTagId(Integer questionId, Integer tagId) {
            this.questionId = questionId;
            this.tagId = tagId;
        }

        public Integer getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Integer questionId) {
            this.questionId = questionId;
        }

        public Integer getTagId() {
            return tagId;
        }

        public void setTagId(Integer tagId) {
            this.tagId = tagId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QuestionTagId that = (QuestionTagId) o;
            return Objects.equals(questionId, that.questionId) &&
                    Objects.equals(tagId, that.tagId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(questionId, tagId);
        }
    }
}