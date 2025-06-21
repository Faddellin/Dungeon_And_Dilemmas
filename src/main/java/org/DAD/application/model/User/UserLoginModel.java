package org.DAD.application.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginModel {
    public String email;
    public String password;
}
