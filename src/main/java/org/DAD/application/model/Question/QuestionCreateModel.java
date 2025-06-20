package org.DAD.application.model.Question;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.DAD.domain.entity.Question.QuestionType;

import java.util.UUID;

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



}
