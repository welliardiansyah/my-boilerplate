package com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.repository;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByPassword(String password);
}
