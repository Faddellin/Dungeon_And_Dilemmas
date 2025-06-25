package org.DAD.application.model.Connection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotifyReadyMessage extends MessageWrapper {
    private boolean isReady;
}
