package org.DAD.application.model.Connection.FromBack;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;

@Data
@NoArgsConstructor
public class AnswerResultMessage extends MessageWrapper {
    private boolean isCorrect;
    private String correctAnswer;
}
