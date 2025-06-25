package org.DAD.application.model.Connection.FromBack;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;

import java.util.Map;

@Data
@NoArgsConstructor
public class GameEndedMessage extends MessageWrapper {
    private Map<String, Integer> scores;
}
