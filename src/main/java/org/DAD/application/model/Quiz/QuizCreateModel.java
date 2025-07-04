package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.DAD.domain.entity.Quiz.QuizDifficulty;

@Data
public class QuizCreateModel {

    @NotBlank
    @Size(min = 1, max = 64)
    private String title;

    private String description;

    @NotNull
    private QuizDifficulty quizDifficulty;

}
