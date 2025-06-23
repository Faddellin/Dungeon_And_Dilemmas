package org.DAD.application.model.Answer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.DAD.domain.entity.Answer.AnswerType;


@Data
public class TextAnswerCreateModel
        extends AnswerCreateModel {

    @NotBlank
    private String text;

}
