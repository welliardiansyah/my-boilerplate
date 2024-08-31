package com.boilerplate.welli.Created.By.Welli.Ardiansyah.service;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.PermissionEntity;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.RoleEntity;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.repository.PermissionRepository;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.repository.RoleRepository;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.RoleDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.response.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;

    @Override
    public ResponseEntity<?> create(RoleDto data) {
        try {
            Optional<RoleEntity> existingRole = repository.findByName(data.getName());
            if (existingRole.isPresent()) {
                return ResponseHandler.errorResponseBuilder(
                        "Role name already in use!",
                        HttpStatus.BAD_REQUEST
                );
            }

            Set<PermissionEntity> managedPermissions = new HashSet<>();
            for (PermissionEntity permissionId : data.getPermissions()) {
                Optional<PermissionEntity> existingPermission = permissionRepository.findById(permissionId.getId());

                if (existingPermission.isEmpty()) {
                    return ResponseHandler.errorResponseBuilder(
                            "Permission with ID " + permissionId.toString() + " not found!",
                            HttpStatus.NOT_FOUND
                    );
                }
                managedPermissions.add(existingPermission.get());
            }

            RoleEntity roleToCreate = RoleEntity.builder()
                    .name(data.getName())
                    .permissions(managedPermissions)
                    .build();
            RoleEntity savedRole = repository.save(roleToCreate);

            return ResponseHandler.successResponseBuilder(
                    "Role created successfully!",
                    HttpStatus.OK,
                    savedRole
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to create role: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(UUID id, RoleDto data) {
        try {
            Optional<RoleEntity> roleOptional = repository.findById(id);
            if (roleOptional.isEmpty()) {
                return ResponseHandler.errorResponseBuilder(
                        "Role not found!",
                        HttpStatus.NOT_FOUND
                );
            }

            for (PermissionEntity permission : data.getPermissions()) {
                UUID permissionId = permission.getId();
                Optional<PermissionEntity> exsitcek = permissionRepository.findById(permissionId);

                if (exsitcek.isEmpty()) {
                    return ResponseHandler.errorResponseBuilder(
                            "Permission with ID " + permissionId.toString() + " not found!",
                            HttpStatus.NOT_FOUND
                    );
                }
            }

            RoleEntity existingRole = roleOptional.get();
            existingRole.setName(data.getName());
            existingRole.setPermissions(data.getPermissions());

            RoleEntity updatedRole = repository.save(existingRole);

            return ResponseHandler.successResponseBuilder(
                    "Role updated successfully!",
                    HttpStatus.OK,
                    updatedRole
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to update role: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        try {
            Optional<RoleEntity> permissionOptional = repository.findById(id);
            if (permissionOptional.isEmpty()) {
                return ResponseHandler.errorResponseBuilder(
                        "Permission with ID " + id.toString() + " not found!",
                        HttpStatus.NOT_FOUND
                );
            }
            RoleEntity delRole = permissionOptional.get();
//            repository.findAll().forEach(role -> role.getPermissions().remove(delRole));
            repository.delete(delRole);

            return ResponseHandler.successResponseBuilder(
                    "Permission deleted successfully!",
                    HttpStatus.OK,
                    delRole
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to delete permission: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> details(UUID id) {
        try {
            Optional<?> get = repository.findById(id);
            if (get.isEmpty()) {
                return ResponseHandler.errorResponseBuilder(
                        "Role id cannot be found!",
                        HttpStatus.NOT_FOUND
                );
            }
            RoleEntity rl = (RoleEntity) get.get();
            return ResponseHandler.successResponseBuilder(
                    "Get details role successfully!",
                    HttpStatus.OK,
                    rl
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to get details role: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> paging(int page, int pageSize, String name) {
        try {
            Page<RoleEntity> rolePage;
            long totalCount;

            if (name != null && !name.isEmpty()) {
                rolePage = repository.findByNameContainingIgnoreCase(name, PageRequest.of(page - 1, pageSize));
                totalCount = rolePage.getTotalElements();
            } else {
                rolePage = repository.findAll(PageRequest.of(page - 1, pageSize));
                totalCount = repository.count();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("items", rolePage.getContent());
            response.put("currentPage", rolePage.getNumber() + 1);
            response.put("totalItems", totalCount);
            response.put("totalPages", rolePage.getTotalPages());

            if (rolePage.isEmpty() && page > 1) {
                return ResponseHandler.successResponseBuilder(
                        "Role not found",
                        HttpStatus.NOT_FOUND,
                        response
                );
            }

            if (page > rolePage.getTotalPages()) {
                return ResponseHandler.successResponseBuilder(
                        "Page number exceeds total pages",
                        HttpStatus.NOT_FOUND,
                        response
                );
            }

            return ResponseHandler.successResponseBuilder(
                    "Page role successfully!",
                    HttpStatus.OK,
                    response
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Error retrieving role: " + e.getMessage());
        }
    }
}
