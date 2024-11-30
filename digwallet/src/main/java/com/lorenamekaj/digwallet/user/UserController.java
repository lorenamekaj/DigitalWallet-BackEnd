package com.lorenamekaj.digwallet.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.lorenamekaj.digwallet.user.dtos.UserDto;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public void registerUser(@RequestBody UserDto userDto) {
        User user = new User();
        user.setFullname(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        userService.addUser(user);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        userService.update(id, userDto);
        return ResponseEntity.ok("User updated successfully");
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}