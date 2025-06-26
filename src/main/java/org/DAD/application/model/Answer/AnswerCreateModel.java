package org.DAD.application.model.Answer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.DAD.domain.entity.Answer.AnswerType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "answerType1"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextAnswerCreateModel.class, name = "text")
})
@Data
public abstract class AnswerCreateModel {

    @NotNull
    protected AnswerType answerType;
    @NotNull
    protected Boolean isCorrect;
}
