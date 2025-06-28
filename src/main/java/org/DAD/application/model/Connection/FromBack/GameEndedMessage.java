package org.DAD.application.model.Connection.FromBack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GameEndedMessage extends MessageWrapper {
    private Map<UUID, Integer> scores;
}
