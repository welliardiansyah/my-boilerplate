package com.boilerplate.welli.Created.By.Welli.Ardiansyah.controller;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.PermissionDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permission")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService service;

    @PostMapping("/create")
//    @PreAuthorize("hasAnyAuthority('CREATE', 'ADMIN')")
    public ResponseEntity<?> createPermission(@RequestBody PermissionDto permissionDto) {
        return service.create(permissionDto);
    }

    @PutMapping("/update/{id}")
//    @PreAuthorize("hasAnyAuthority('UPDATE', 'ADMIN')")
    public ResponseEntity<?> updatePermission(@PathVariable UUID id, @RequestBody PermissionDto permissionDto) {
        return service.update(id, permissionDto);
    }

    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasAnyAuthority('DELETE', 'ADMIN')")
    public ResponseEntity<?> deletePermission(@PathVariable UUID id) {
        return service.delete(id);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('READ', 'ADMIN')")
    public ResponseEntity<?> getPermissionById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping("/paging")
//    @PreAuthorize("hasAnyAuthority('READ', 'ADMIN')")
    public ResponseEntity<?> getAllPermissions(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sequence", required = false) Integer sequence,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return service.paging(name, sequence, page, size);
    }
}
