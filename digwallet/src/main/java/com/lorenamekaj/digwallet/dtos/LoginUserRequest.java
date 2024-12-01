package com.lorenamekaj.digwallet.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class LoginUserRequest {

    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;

    @NotBlank(message = "User must have a password")
    @Size(min = 8, max = 15)
    private String password;

    public @NotEmpty(message = "The email address is required.") @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE}) String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "The email address is required.") @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE}) String email) {
        this.email = email;
    }

    public @NotBlank(message = "User must have a password") @Size(min = 8, max = 15) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "User must have a password") @Size(min = 8, max = 15) String password) {
        this.password = password;
    }
}