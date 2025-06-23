package org.DAD.application.model.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditPasswordModel {
    @NotBlank
    @NotNull
    @Size(min = 8, max = 32)
    public String oldPassword;
    @NotBlank
    @NotNull
    @Size(min = 8, max = 32)
    public String newPassword;
}
