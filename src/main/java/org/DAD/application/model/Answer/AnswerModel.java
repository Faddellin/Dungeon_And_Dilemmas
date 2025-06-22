package org.DAD.application.model.Answer;

import jakarta.validation.constraints.NotNull;
import org.DAD.domain.entity.Answer.AnswerType;

public class AnswerModel {

    @NotNull
    private AnswerType answerType;

}
