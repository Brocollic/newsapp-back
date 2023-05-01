package com.brocollic.newsapp.dtos.requests;

import com.brocollic.newsapp.entities.Role;
import com.brocollic.newsapp.utils.EnumValue;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    @Email(message = "email must be a well formatted email address.")
    @NotBlank(message = "email cannot be null and it's trimmed length must greater than zero.")
    private String email;

    @NotBlank(message = "firstname cannot be null and it's trimmed length must greater than zero.")
    private String firstname;

    @NotBlank(message = "lastname cannot be null and it's trimmed length must greater than zero.")
    private String lastname;

    @NotBlank(message = "password cannot be null and it's trimmed length must greater than zero.")
    @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters long.")
    private String password;

    @EnumValue(enumClass = Role.class, message = "must be ADMIN, CONTENT_CREATOR or DEFAULT_USER")
    @NotNull(message = "role cannot be null.")
    private String role;

}
