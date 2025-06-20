package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuizCreateModel {

    @NotBlank
    @Min(1)
    @Max(64)
    private String title;

    private String description;

}
