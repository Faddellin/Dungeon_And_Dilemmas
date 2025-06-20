package org.DAD.application.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenRequestModel {
    public String refreshToken;
}
