package org.DAD.application.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditModel {
    public String newUserName;
    public String newEmail;
}
