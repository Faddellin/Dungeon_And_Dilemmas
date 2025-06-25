package org.DAD.application.model.Answer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.DAD.domain.entity.Answer.AnswerType;
import org.DAD.domain.entity.Answer.TextAnswer;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "answerType1"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextAnswerModel.class, name = "text")
})
@Data
public abstract class AnswerModel {

    @NotNull
    protected UUID id;

    @NotNull
    @JsonProperty("answerType")
    protected AnswerType answerType;

}
