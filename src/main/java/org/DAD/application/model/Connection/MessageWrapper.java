package org.DAD.application.model.Connection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.FromBack.*;
import org.DAD.application.model.Connection.FromFront.AnswerMessage;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayerJoinedMessage.class, name = "PlayerJoinedMessage"),
        @JsonSubTypes.Type(value = NotifyReadyMessage.class, name = "NotifyReadyMessage"),
        @JsonSubTypes.Type(value = AnswerMessage.class, name = "AnswerMessage"),
        @JsonSubTypes.Type(value = AnswerResultMessage.class, name = "AnswerResultMessage"),
        @JsonSubTypes.Type(value = QuestionMessage.class, name = "QuestionMessage"),
        @JsonSubTypes.Type(value = GameStartedMessage.class, name = "GameStartedMessage"),
        @JsonSubTypes.Type(value = GameEndedMessage.class, name = "GameEndedMessage"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "ErrorMessage"),
        @JsonSubTypes.Type(value = PlayerLeftMessage.class, name = "PlayerLeftMessage")
})
@Data
@NoArgsConstructor
public abstract class MessageWrapper {
    private String playerId;
}
