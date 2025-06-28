package org.DAD.application.model.Connection.FromBack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GameStartedMessage extends MessageWrapper {
    private List<String> playerIds;
}
