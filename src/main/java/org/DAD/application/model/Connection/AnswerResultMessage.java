package org.DAD.application.model.Connection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerResultMessage extends MessageWrapper {
    private boolean isCorrect;
    private String correctAnswer;
}
