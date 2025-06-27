package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.DAD.application.model.Question.QuestionModel;
import org.DAD.application.model.User.UserShortModel;
import org.DAD.domain.entity.Quiz.QuizDifficulty;
import org.DAD.domain.entity.Quiz.QuizStatus;

import java.util.List;
import java.util.UUID;

@Data
public class QuizDetailModel {

    @NotNull
    private UUID id;

    @NotBlank
    @Size(min = 1, max = 64)
    private String title;

    private String description;

    @NotNull
    private QuizDifficulty difficulty;

    @NotNull
    private QuizStatus status;

    @NotNull
    private UserShortModel creator;

    private List<QuestionModel> questions;
} 