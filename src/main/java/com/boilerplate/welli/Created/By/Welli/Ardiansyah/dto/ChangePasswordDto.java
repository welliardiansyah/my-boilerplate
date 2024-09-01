package com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto;

import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class ChangePasswordDto {
    private String oldPassword;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "New password must be at least 8 characters long and include at least one uppercase letter, one digit, and one special character")
    private String newPassword;
}
