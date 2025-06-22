package org.DAD.application.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserOtherModel {
    public UUID id;
    public String userName;
    public Integer totalPoints;
    public BestUsersGameModel bestUsersGame;
}
