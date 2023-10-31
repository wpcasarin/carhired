package org.ifsul.carhired.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRegisterDTO {
    @Email
    @NotNull
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Size(min = 11, max = 11, message = "must have 11 characters")
    @Pattern(regexp = "\\d+", message = "must have only numbers")
    private String cpf;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String address;
}
