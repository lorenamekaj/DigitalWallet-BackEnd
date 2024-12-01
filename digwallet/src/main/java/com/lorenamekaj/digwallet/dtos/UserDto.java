package com.lorenamekaj.digwallet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotNull @Size(min = 2, max = 50) String name,
        @NotNull @Email String email,
        @NotNull @Size(min = 6) String password
) {}
