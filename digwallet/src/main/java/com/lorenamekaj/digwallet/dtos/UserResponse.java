package com.lorenamekaj.digwallet.dtos;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class UserResponse {

    Long id;

    String role;

    String name;

    String email;

}