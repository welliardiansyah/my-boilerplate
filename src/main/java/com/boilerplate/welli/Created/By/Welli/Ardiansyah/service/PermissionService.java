package com.boilerplate.welli.Created.By.Welli.Ardiansyah.service;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.PermissionDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PermissionService {
    ResponseEntity<?> create(PermissionDto data);
    ResponseEntity<?> update(UUID id, PermissionDto data);
    ResponseEntity<?> delete(UUID id);
    ResponseEntity<?> findById(UUID id);
    ResponseEntity<?> paging(String name, Integer sequence, int page, int size);
}
