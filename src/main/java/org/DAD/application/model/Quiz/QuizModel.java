package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.DAD.application.model.UserModel;
import org.DAD.domain.entity.Quiz.QuizDifficulty;
import org.DAD.domain.entity.Quiz.QuizStatus;

import java.util.UUID;

@Data
public class QuizModel {

    @NotNull
    private UUID id;

    @NotBlank
    @Min(1)
    @Max(64)
    private String title;

    private String description;

    @NotNull
    private QuizDifficulty difficulty;

    @NotNull
    private QuizStatus status;

    @NotNull
    private UserModel userModel;

}
