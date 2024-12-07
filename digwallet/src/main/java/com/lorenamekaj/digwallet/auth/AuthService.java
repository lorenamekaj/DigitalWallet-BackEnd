package com.lorenamekaj.digwallet.auth;

import com.lorenamekaj.digwallet.dtos.UserDto;
import com.lorenamekaj.digwallet.mappers.UserDtoMapper;
import com.lorenamekaj.digwallet.security.jwt.JwtUtil;
import com.lorenamekaj.digwallet.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.lorenamekaj.digwallet.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDtoMapper mapper;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserDtoMapper mapper, JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User principal = (User) authentication.getPrincipal();
        UserDto userDto = mapper.apply(principal);
        String token = jwtUtil.issueToken(userDto.email(), userDto.role());
        return new AuthResponse(token, userDto);
    }

    public void register(AuthRegisterRequest request) {
        User user = new User(
                request.name(), request.email(), passwordEncoder.encode(request.password()), "USER"
        );
        userService.addUser(user);
    }

    public UserDto verifyUser(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User principal = (User) authentication.getPrincipal();
        return mapper.apply(principal);
    }

}
