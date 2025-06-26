package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.DAD.domain.entity.Quiz.QuizDifficulty;

@Data
public class QuizFiltersModel {

    private String title;

    private String description;

    private QuizDifficulty difficulty;

    private String creatorEmail;

    @Min(1)
    private Integer page;

    @Min(1)
    private Integer pageSize;

}
