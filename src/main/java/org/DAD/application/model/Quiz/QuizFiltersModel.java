package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.DAD.domain.entity.Quiz.QuizDifficulty;

@Data
public class QuizFiltersModel {

    private String title;

    private String description;

    private QuizDifficulty difficulty;

    @Email
    private String creatorEmail;

    private Integer page;

    private Integer pageSize;

}
