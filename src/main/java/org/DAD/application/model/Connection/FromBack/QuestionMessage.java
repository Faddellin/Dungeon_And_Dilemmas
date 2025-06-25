package org.DAD.application.model.Connection.FromBack;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.model.Question.QuestionModel;

@Data
@NoArgsConstructor
public class QuestionMessage extends MessageWrapper {
    private QuestionModel questionModel;
}
