package com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.RoleEntity;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
public class RegisterDto {
    @NonNull
    private String fullname;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private Set<RoleEntity> roles;
}
