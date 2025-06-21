package org.DAD.application.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseModel {
    public String accessToken;
    public String refreshToken;
}