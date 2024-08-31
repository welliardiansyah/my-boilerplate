package com.boilerplate.welli.Created.By.Welli.Ardiansyah.service;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.RoleDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface RoleService {
    ResponseEntity<?> create(RoleDto data);
    ResponseEntity<?> update(UUID id, RoleDto data);
    ResponseEntity<?> delete(UUID id);
    ResponseEntity<?> details(UUID id);
    ResponseEntity<?> paging(int page, int pageSize, String name);
}
