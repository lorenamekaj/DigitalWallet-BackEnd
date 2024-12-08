package com.lorenamekaj.digwallet.auth;

import com.lorenamekaj.digwallet.dtos.CreateUserRequest;
import com.lorenamekaj.digwallet.dtos.LoginUserRequest;
import com.lorenamekaj.digwallet.exceptions.ResourceNotFoundException;
import com.lorenamekaj.digwallet.profile.ProfileService;
import com.lorenamekaj.digwallet.user.User;
import com.lorenamekaj.digwallet.user.UserRepository;
import com.lorenamekaj.digwallet.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthenticationService {

    public AuthenticationService(UserRepository userRepository, UserService userService, ProfileService profileService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    private final UserRepository userRepository;

    private final UserService userService;

    private final ProfileService profileService;

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

        User user_ = userService.getUserByEmail(input.getEmail());

        profileService.addProfile(user_);

        return Optional.of(user_);
    }

    public User authenticate(LoginUserRequest input) {
        User user = userRepository.findUserByEmail(input.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        return user;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}