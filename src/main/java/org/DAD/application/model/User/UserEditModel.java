package org.DAD.application.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditModel {
    @NotNull
    @NotBlank
    @Size(min = 4, max = 32)
    public String newUserName;
    @Email
    @NotBlank
    @NotNull
    public String newEmail;
}
