package com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.repository;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.PermissionEntity;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface PermissionRepository extends JpaRepository<PermissionEntity, UUID> {
    Page<PermissionEntity> findByNameContainingAndSequence(String name, Integer sequence, Pageable pageable);
    Page<PermissionEntity> findByNameContaining(String name, Pageable pageable);
    Page<PermissionEntity> findBySequence(Integer sequence, Pageable pageable);
    Page<PermissionEntity> findAll(Pageable pageable);
}
