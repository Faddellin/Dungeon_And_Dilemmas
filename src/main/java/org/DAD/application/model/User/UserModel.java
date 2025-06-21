package org.DAD.application.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "Модель пользователя")
public class UserModel {
    @Schema(description = "ID пользователя", example = "3a063a3f-58d1-4b27-a9da-ed0544a78d06")
    public UUID id;
    @Schema(description = "Юзернейм пользователя", example = "чикатила")
    public String userName;
    @Schema(description = "Суммарное количество очков", example = "1234")
    public Integer totalPoints;
    @Schema(description = "Лучшая игра пользователя", implementation = BestUsersGameModel.class)
    public BestUsersGameModel bestUsersGame;
}
