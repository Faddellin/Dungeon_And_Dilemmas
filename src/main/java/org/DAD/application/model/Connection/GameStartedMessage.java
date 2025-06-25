package org.DAD.application.model.Connection;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GameStartedMessage extends MessageWrapper {
    private List<String> playerIds;
}
