package com.boilerplate.welli.Created.By.Welli.Ardiansyah.controller;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.RoleDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<?> created(@RequestBody RoleDto data) {
        return service.create(data);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<?> updated(@PathVariable("id") UUID id, @RequestBody RoleDto data) {
        return service.update(id, data);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<?> deleted(@PathVariable("id") UUID id) {
        return service.delete(id);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<?> detail(@PathVariable("id") UUID id) {
        return service.details(id);
    }

    @GetMapping("/paging")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<?> paging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name
    ) {
        return service.paging(page, pageSize, name);
    }
}
