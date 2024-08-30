package com.boilerplate.welli.Created.By.Welli.Ardiansyah.service;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.PermissionEntity;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.repository.PermissionRepository;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.PermissionDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.response.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService{
    private final PermissionRepository repository;

    @Override
    public ResponseEntity<?> create(PermissionDto data) {
        try {
            PermissionEntity created = PermissionEntity.builder()
                    .name(data.getName())
                    .sequence(data.getSequence())
                    .build();

            PermissionEntity saved = repository.save(created);

            return ResponseHandler.successResponseBuilder(
                    "Created data permission successfully!",
                    HttpStatus.OK,
                    saved
            );
        } catch (Exception e) {
            return ResponseHandler.errorResponseBuilder(
                    "Create permission failed" + e.getMessage(),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }

    @Override
    public ResponseEntity<?> update(UUID id, PermissionDto data) {
        Optional<?> findExist = repository.findById(id);

        if(findExist.isEmpty()) {
            return ResponseHandler.errorResponseBuilder(
                    "Id permission cannot be found!",
                    HttpStatus.NOT_FOUND
            );
        }
        try {
            PermissionEntity perms = (PermissionEntity) findExist.get();
            perms.setName(data.getName());
            perms.setSequence(data.getSequence());

            PermissionEntity updated = repository.save(perms);

            return ResponseHandler.successResponseBuilder(
                    "Updated data permission successfully!",
                    HttpStatus.OK,
                    updated
            );
        } catch (Exception e) {
            return ResponseHandler.errorResponseBuilder(
                    "Updated permission failed" + e.getMessage(),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        Optional<?> findExist = repository.findById(id);

        if(findExist.isEmpty()) {
            return ResponseHandler.errorResponseBuilder(
                    "Id permission cannot be found!",
                    HttpStatus.NOT_FOUND
            );
        }
        try {
            PermissionEntity perms = (PermissionEntity) findExist.get();
            repository.deleteById(perms.getId());
            return ResponseHandler.successResponseBuilder(
                    "Updated data permission successfully!",
                    HttpStatus.OK,
                    perms
            );
        } catch (Exception e) {
            return ResponseHandler.errorResponseBuilder(
                    "Delete permission failed" + e.getMessage(),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }

    @Override
    public ResponseEntity<?> findById(UUID id) {
        try {
            Optional<?> findExist = repository.findById(id);
            PermissionEntity perms = (PermissionEntity) findExist.get();

            return ResponseHandler.successResponseBuilder(
                    "Updated data permission successfully!",
                    HttpStatus.OK,
                    perms
            );
        } catch (Exception e) {
            return ResponseHandler.errorResponseBuilder(
                    "Get permission by UUID failed" + e.getMessage(),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }

    @Override
    public ResponseEntity<?> paging(String name, Integer sequence, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            Page<PermissionEntity> resultPage;

            if (name != null && !name.isEmpty() && sequence != null) {
                resultPage = repository.findByNameContainingAndSequence(name, sequence, pageable);
            } else if (name != null && !name.isEmpty()) {
                resultPage = repository.findByNameContaining(name, pageable);
            } else if (sequence != null) {
                resultPage = repository.findBySequence(sequence, pageable);
            } else {
                resultPage = repository.findAll(pageable);
            }

            return ResponseHandler.successResponseBuilder(
                    "Updated data permission successfully!",
                    HttpStatus.OK,
                    resultPage
            );
        } catch (Exception e) {
            return ResponseHandler.errorResponseBuilder(
                    "Paging permissions failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
