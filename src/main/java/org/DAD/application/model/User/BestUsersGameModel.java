package org.DAD.application.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Лучшая игра пользователя")
public class BestUsersGameModel {
    @Schema(description = "Название игры", example = "игра")
    public String name;
    @Schema(description = "Счёт", example = "123")
    public int score;
}
