package org.DAD.application.model.Answer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TextAnswerModel
        extends AnswerModel{

    @NotBlank
    private String text;

}
