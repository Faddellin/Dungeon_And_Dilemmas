package org.DAD.application.model.Connection;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class GameEndedMessage extends MessageWrapper {
    private Map<String, Integer> scores;
}
