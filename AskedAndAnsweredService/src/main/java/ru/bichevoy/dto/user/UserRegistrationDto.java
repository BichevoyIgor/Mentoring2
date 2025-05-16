package ru.bichevoy.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    @Size(min = 4)
    private String email;
}
