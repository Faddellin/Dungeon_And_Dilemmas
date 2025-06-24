package org.DAD.application.model.Connection;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Answer.AnswerSimpleModel;

import java.util.List;

@Data
@NoArgsConstructor
public class QuestionMessage extends MessageWrapper {
    private String questionId;
    private String questionText;
    private List<AnswerSimpleModel> options;
    private Integer timeLimit;
}
