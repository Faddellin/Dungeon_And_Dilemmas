package org.DAD.application.model.Connection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerLeftMessage extends MessageWrapper {
    private UUID leftPlayerId;
}
