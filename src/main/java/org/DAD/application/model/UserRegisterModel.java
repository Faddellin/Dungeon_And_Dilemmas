package org.DAD.application.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterModel {
    public String userName;
    public String email;
    public String password;
}
