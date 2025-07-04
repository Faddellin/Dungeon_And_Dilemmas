package org.DAD.application.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenResponseModel {
    public String accessToken;
    public String refreshToken;
}