package com.lorenamekaj.digwallet.auth;

import com.lorenamekaj.digwallet.dtos.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        AuthResponse response = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody AuthRegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.ok()
                .body("User registered successfully!");
    }

    @PreAuthorize("#request.username == authentication.principal.username")
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody AuthRequest request) {
        UserDto dto = authenticationService.verifyUser(request);
        return ResponseEntity.ok()
                .body(dto);
    }
}