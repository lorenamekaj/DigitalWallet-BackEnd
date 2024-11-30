package com.lorenamekaj.digwallet.user;

import com.lorenamekaj.digwallet.exceptions.DuplicateResourceException;
import com.lorenamekaj.digwallet.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import com.lorenamekaj.digwallet.user.dtos.UserDto;

@Service

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("user with email already taken");
        }
        userRepository.save(user);
    }


    @Transactional
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public User getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + id + " not found"));
    }

    @Transactional
    public void deleteById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);

    }

    @Transactional
    public void update(Long id, UserDto userDto) {
        User user = getUser(id);

        boolean changes = false;

        if (userDto.name() != null && !userDto.name().equals(user.getFullname())) {
            user.setFullname(userDto.name());
            changes = true;
        }

        if (userDto.email() != null && !userDto.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDto.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            user.setEmail(userDto.email());
            changes = true;
        }

        if (userDto.password() != null && !userDto.password().equals(user.getPassword())) {
            user.setPassword(userDto.password());
            changes = true;
        }

        if (!changes) {
            throw new RuntimeException("No changes detected");
        }

    }



}