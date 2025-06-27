package org.DAD.application.model.Connection.FromBack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerJoinedMessage extends MessageWrapper {
    private UUID joinedPlayerId;
    private String joinedPlayerName;
    private Boolean playerIsReady;
}
