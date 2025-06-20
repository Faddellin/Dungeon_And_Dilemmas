package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.DAD.domain.entity.Quiz.QuizDifficulty;

@Data
public class QuizFiltersModel {

    private String title;

    private String description;

    private QuizDifficulty difficulty;

    @Email
    private String creatorEmail;

}
