package org.DAD.application.model.Question;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.DAD.domain.entity.Question.QuestionType;

@Data
public class QuestionCreateModel {

    @NotNull @Min(20) @Max(60)
    private Integer duration;

    @NotNull @Min(1) @Max(30)
    private Integer damage;

    @NotNull @Min(1) @Max(100)
    private Integer reward;

    @NotNull
    private QuestionType questionType;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 256)
    private String questionText;
}
