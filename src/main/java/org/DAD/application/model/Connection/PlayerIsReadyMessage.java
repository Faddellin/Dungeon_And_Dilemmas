package org.DAD.application.model.Connection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerIsReadyMessage extends MessageWrapper {
    private String joinedPlayerName;
}
