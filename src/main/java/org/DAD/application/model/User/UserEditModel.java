package org.DAD.application.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditModel {
    public String newUserName;
    public String newEmail;
}
