package com.lorenamekaj.digwallet.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AuthRegisterRequest(
        @NotNull @Min(3) @Max(30) String name,
        @NotNull @Email String email,
        @NotNull String password
) {
}
