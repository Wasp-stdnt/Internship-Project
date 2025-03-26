package io.github.waspstdnt.wishlist_app.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDto {

    @NotBlank(message = "Ім'я користувача - обов'язкове")
    private String username;

    @NotBlank(message = "Емейл - обов'язковий")
    @Email(message = "Емейл повиннен відповідати формату емейлів")
    private String email;

    @NotBlank(message = "Пароль - обов'язковий")
    @Size(min = 6, message = "Пароль повинен бути довший за 5 символів")
    private String password;
}
