package com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginDto {
    @NonNull
    private String email;

    @NonNull
    private String password;
}
