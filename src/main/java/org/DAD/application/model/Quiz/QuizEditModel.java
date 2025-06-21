package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuizEditModel {

    @NotBlank
    @Size(min = 1, max = 64)
    private String title;

    private String description;

}
