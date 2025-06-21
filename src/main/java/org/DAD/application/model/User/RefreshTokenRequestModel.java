package org.DAD.application.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenRequestModel {
    public String refreshToken;
}
