package org.DAD.application.model.Answer;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.DAD.domain.entity.Answer.AnswerType;

@Data
public class TextAnswerModel
        extends AnswerModel{

    @NotBlank
    private String text;

}
