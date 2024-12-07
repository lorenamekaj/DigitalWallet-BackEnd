package com.lorenamekaj.digwallet.user;

import com.lorenamekaj.digwallet.dtos.UserDto;
import com.lorenamekaj.digwallet.exceptions.DuplicateResourceException;
import com.lorenamekaj.digwallet.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import com.lorenamekaj.digwallet.dtos.UserDto;

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
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email: " + email + " not found"));
    }

    @Transactional
    public void deleteById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);

    }
}