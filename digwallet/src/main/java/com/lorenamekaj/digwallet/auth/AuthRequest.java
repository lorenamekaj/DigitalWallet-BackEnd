package com.lorenamekaj.digwallet.auth;
public record AuthRequest(
        String email, String password
) {
}