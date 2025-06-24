package org.DAD.application.model.Connection;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
public class UserAnswerModel
    extends ModelWrapper{
    @NotNull
    private UUID userId;
    @NotNull
    private UUID answerId;
}
