package org.DAD.application.model.Connection.FromBack;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.application.model.Connection.MessageWrapper;

@Data
@NoArgsConstructor
public class ErrorMessage extends MessageWrapper {
    private String message;
}
