package com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.PermissionEntity;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
public class RoleDto {
    @NonNull
    private String name;

    @NonNull
    private Set<PermissionEntity> permissions;
}
