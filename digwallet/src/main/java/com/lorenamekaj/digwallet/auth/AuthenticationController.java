package com.lorenamekaj.digwallet.auth;

import com.lorenamekaj.digwallet.dtos.CreateUserRequest;
import com.lorenamekaj.digwallet.dtos.LoginResponse;
import com.lorenamekaj.digwallet.dtos.LoginUserRequest;
import com.lorenamekaj.digwallet.dtos.RegisterUserRequest;
import com.lorenamekaj.digwallet.exceptions.UserException;
import com.lorenamekaj.digwallet.security.jwt.JwtService;
import com.lorenamekaj.digwallet.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterUserRequest registerUserRequest) {

        CreateUserRequest createUserRequest = new CreateUserRequest(
                registerUserRequest.getName(),
                registerUserRequest.getEmail(),
                registerUserRequest.getPassword(),
                "USER"
        );

        authenticationService.signup(createUserRequest).orElseThrow(() -> new UserException("User registration failed."));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        User authenticatedUser = authenticationService.authenticate(loginUserRequest);

        String accessToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String refreshToken = authHeader.substring(7);
        String username = jwtService.extractUsername(refreshToken);

        if (username != null && jwtService.isTokenValid(refreshToken, authenticationService.loadUserByUsername(username))) {
            UserDetails user = authenticationService.loadUserByUsername(username);
            String newAccessToken = jwtService.generateToken(user);

            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "expiresIn", String.valueOf(jwtService.getExpirationTime())
            ));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
