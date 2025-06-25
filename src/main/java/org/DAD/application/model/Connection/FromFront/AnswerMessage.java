package org.DAD.application.model.Connection.FromFront;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;

@Data
@NoArgsConstructor
public class AnswerMessage extends MessageWrapper {
    private String questionId;
    private String answerId;
}
