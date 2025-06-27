package org.DAD.application.model.Question;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.DAD.application.model.Answer.AnswerModel;
import org.DAD.domain.entity.Question.QuestionType;

import java.util.List;
import java.util.UUID;

@Data
public class QuestionModel {

    @NotNull
    private UUID id;

    @NotNull @Min(20) @Max(60)
    private Integer duration;

    @NotNull @Min(1) @Max(30)
    private Integer damage;

    @NotNull @Min(1) @Max(100)
    private Integer reward;

    @NotNull
    private QuestionType questionType;

    @NotNull
    @Size(min = 4, max = 256)
    private String questionText;

    private Integer questionNumber;

    private List<AnswerModel> answers;

}
