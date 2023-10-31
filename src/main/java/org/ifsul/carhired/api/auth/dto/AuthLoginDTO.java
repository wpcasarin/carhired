package org.ifsul.carhired.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthLoginDTO {
    @Email
    @NotNull
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;
}
