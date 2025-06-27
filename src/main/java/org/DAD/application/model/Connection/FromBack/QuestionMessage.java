package org.DAD.application.model.Connection.FromBack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.model.Question.QuestionModel;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class QuestionMessage extends MessageWrapper {
    private QuestionModel questionModel;
}
