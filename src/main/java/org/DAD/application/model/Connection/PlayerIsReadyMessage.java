package org.DAD.application.model.Connection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlayerIsReadyMessage extends MessageWrapper {
    private UUID readyPlayerId;
    private Boolean isReady;
}
