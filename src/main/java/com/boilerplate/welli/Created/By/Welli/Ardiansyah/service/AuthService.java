package com.boilerplate.welli.Created.By.Welli.Ardiansyah.service;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.ChangePasswordDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.LoginDto;
import com.boilerplate.welli.Created.By.Welli.Ardiansyah.dto.RegisterDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AuthService {
    ResponseEntity<?> register(RegisterDto data);
    ResponseEntity<?> login(LoginDto data);
    ResponseEntity<?> profile();
    ResponseEntity<?> changePassword(UUID id, ChangePasswordDto data);
}
