package org.DAD.application.model.Group;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.User.UserShortModel;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class GroupModel {

    @NotNull
    private UUID id;

    @NotNull
    private UUID ownerId;

    @NotNull
    @Size(min = 5, max = 5)
    private String code;

    @Size(min = 1, max = 4)
    private List<UserShortModel> members;

    private UUID selectedQuizId;
}
