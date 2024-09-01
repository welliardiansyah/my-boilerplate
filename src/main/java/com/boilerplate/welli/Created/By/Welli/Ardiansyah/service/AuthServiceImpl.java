package com.boilerplate.welli.Created.By.Welli.Ardiansyah.service;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.config.jwt.JwtUtils;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.config.jwt.service.UserDetailsImpl;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.RoleEntity;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.UserEntity;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.repository.AuthRepository;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.repository.RoleRepository;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.ChangePasswordDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.LoginDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.RegisterDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.response.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> register(RegisterDto data) {
        try {
            // Check if email already exists
            Optional<?> existingUser = repository.findByEmail(data.getEmail());
            if (existingUser.isPresent()) {
                return ResponseHandler.errorResponseBuilder(
                        "Email already in use!",
                        HttpStatus.BAD_REQUEST
                );
            }

            List<RoleEntity> managedRoles = new ArrayList<>();
            for (RoleEntity role : data.getRoles()) {
                UUID roleId = role.getId();
                Optional<RoleEntity> existingRole = roleRepository.findById(roleId);
                if (existingRole.isEmpty()) {
                    return ResponseHandler.errorResponseBuilder(
                            "Role with ID " + roleId.toString() + " not found!",
                            HttpStatus.NOT_FOUND
                    );
                }
                managedRoles.add(existingRole.get());
            }

            UserEntity created = UserEntity.builder()
                    .fullname(data.getFullname())
                    .email(data.getEmail())
                    .password(encoder.encode(data.getPassword()))
                    .roles(new HashSet<>(managedRoles))
                    .build();
            UserEntity saved = repository.save(created);

            return ResponseHandler.successResponseBuilder(
                    "Created user successfully!",
                    HttpStatus.OK,
                    saved
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to register user: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> login(LoginDto data) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwtToken = jwtUtils.generateJwtToken(authentication);

            Map<String, Object> userResponse = new TreeMap<>();
            userResponse.put("id", userDetails.getId());
            userResponse.put("fullname", userDetails.getFullname());
            userResponse.put("email", userDetails.getEmail());
            userResponse.put("authorities", userDetails.getAuthorities());
            userResponse.put("permissions", userDetails.getPermissions());
            userResponse.put("enabled", userDetails.isEnabled());
            userResponse.put("username", userDetails.getUsername());
            userResponse.put("accountNonExpired", userDetails.isAccountNonExpired());
            userResponse.put("accountNonLocked", userDetails.isAccountNonLocked());
            userResponse.put("credentialsNonExpired", userDetails.isCredentialsNonExpired());
            userResponse.put("token", jwtToken);

            return ResponseHandler.successResponseBuilder(
                    "Login users successfully!",
                    HttpStatus.OK,
                    userResponse
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Failed to login user: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> profile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Map<String, Object> profile = new TreeMap<>();
            profile.put("id", userDetails.getId());
            profile.put("fullname", userDetails.getFullname());
            profile.put("email", userDetails.getEmail());
            profile.put("authorities", userDetails.getAuthorities());
            profile.put("permissions", userDetails.getPermissions());
            profile.put("enabled", userDetails.isEnabled());
            profile.put("username", userDetails.getUsername());
            profile.put("accountNonExpired", userDetails.isAccountNonExpired());
            profile.put("accountNonLocked", userDetails.isAccountNonLocked());
            profile.put("credentialsNonExpired", userDetails.isCredentialsNonExpired());

            return ResponseHandler.successResponseBuilder(
                    "Get user profile successfully!",
                    HttpStatus.OK,
                    profile
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Failed to get profile user: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> changePassword(UUID id, ChangePasswordDto data) {
        try {
            if (data == null || id == null || data.getNewPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid request data");
            }

            boolean isOldPasswordValid = isTruePassword(id, data.getOldPassword());
            if (!isOldPasswordValid) {
                return ResponseHandler.errorResponseBuilder(
                        "Old password does not match",
                        HttpStatus.UNAUTHORIZED
                );
            }

            if (!data.getNewPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$")) {
                return ResponseHandler.errorResponseBuilder(
                        "New password does not meet complexity requirements",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (repository.existsByPassword(encoder.encode(data.getNewPassword()))) {
                return ResponseHandler.errorResponseBuilder(
                        "New password must be unique",
                        HttpStatus.BAD_REQUEST
                );
            }

            Optional<UserEntity> getUser = repository.findById(id);
            if (getUser.isPresent()) {
                UserEntity user = getUser.get();
                user.setPassword(encoder.encode(data.getNewPassword()));
                UserEntity saved = repository.save(user);
                saved.setPassword(null);

                return ResponseHandler.successResponseBuilder(
                        "Update user password successfully!",
                        HttpStatus.OK,
                        saved
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "User not found",
                        HttpStatus.NOT_FOUND
                );
            }
        } catch (Exception e) {
            return ResponseHandler.errorResponseBuilder(
                    "Failed to change password",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public boolean isTruePassword(UUID userId, String oldPassword) {
        Optional<UserEntity> check = repository.findById(userId);
        if (check.isPresent()) {
            UserEntity u = check.get();
            return encoder.matches(oldPassword, u.getPassword());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User cannot be found!");
        }
    }
}
