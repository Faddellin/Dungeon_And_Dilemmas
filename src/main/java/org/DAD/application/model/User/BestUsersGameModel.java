package org.DAD.application.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BestUsersGameModel {
    public String name;
    public int score;
}
