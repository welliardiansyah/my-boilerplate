package com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.repository;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID>, JpaSpecificationExecutor<RoleEntity> {
    Optional<RoleEntity> findByName(String name);
    Page<RoleEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
