package com.lorenamekaj.digwallet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserRegisterDto(
        @NotNull @Min(2) @Max(30) String name,
        @NotNull @Email String email,
        @NotNull @Min(6) @Max(30) String password
) {
}
