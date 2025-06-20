package org.DAD.application.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditPasswordModel {
    public String oldPassword;
    public String newPassword;
}
