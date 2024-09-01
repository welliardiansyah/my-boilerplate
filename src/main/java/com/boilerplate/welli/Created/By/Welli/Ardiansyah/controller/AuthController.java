package com.boilerplate.welli.Created.By.Welli.Ardiansyah.controller;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.config.jwt.JwtUtils;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.ChangePasswordDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.LoginDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.RegisterDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.response.ResponseHandler;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    private final JwtUtils jwtUtils;

    @PostMapping("/created")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> created(HttpServletRequest request, @RequestBody @Valid RegisterDto data) {
        return service.register(data);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto data) {
        return service.login(data);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        return ResponseHandler.successResponseBuilder(
                "You've been signed out!",
                HttpStatus.OK,
                cookie.toString()
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        return service.profile();
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable("id") UUID id, @RequestBody @Valid ChangePasswordDto data) {
        return service.changePassword(id, data);
    }
}
