package org.DAD.application.model.Connection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerJoinedMessage extends MessageWrapper {
    private String joinedPlayerName;
}
