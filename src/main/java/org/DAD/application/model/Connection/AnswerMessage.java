package org.DAD.application.model.Connection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerMessage extends MessageWrapper {
    private String questionId;
    private String answerId;
}
