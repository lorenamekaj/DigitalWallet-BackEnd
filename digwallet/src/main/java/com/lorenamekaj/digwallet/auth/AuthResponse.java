package com.lorenamekaj.digwallet.auth;

import com.lorenamekaj.digwallet.dtos.UserDto;

public record AuthResponse(
        String token, UserDto userDto
) {
}
