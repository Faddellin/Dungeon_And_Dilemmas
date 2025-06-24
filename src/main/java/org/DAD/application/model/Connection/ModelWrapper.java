package org.DAD.application.model.Connection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Answer.TextAnswerCreateModel;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserAnswerModel.class, name = "UserAnswerModel"),
        @JsonSubTypes.Type(value = UserCheckModel.class, name = "UserCheckModel")
})
@Data
@NoArgsConstructor
public abstract class ModelWrapper {
    private String type;
}
