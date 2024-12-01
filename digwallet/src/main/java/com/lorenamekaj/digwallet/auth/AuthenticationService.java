package com.lorenamekaj.digwallet.auth;

import com.lorenamekaj.digwallet.dtos.CreateUserRequest;
import com.lorenamekaj.digwallet.dtos.LoginUserRequest;
import com.lorenamekaj.digwallet.exceptions.ResourceNotFoundException;
import com.lorenamekaj.digwallet.user.User;
import com.lorenamekaj.digwallet.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public Optional<User> signup(CreateUserRequest input) {

        if (userRepository.existsByEmail(input.getEmail())) {
            throw new ResourceNotFoundException("User with email " + input.getEmail() + " already exists.");
        }
        User user = new User();

        user.setFullname(input.getName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        String role = "USER";
        user.setRole(role);

        userRepository.save(user);

        return Optional.of(user);
    }

    public User authenticate(LoginUserRequest input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        return user;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}