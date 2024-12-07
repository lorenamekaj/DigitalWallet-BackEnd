package com.lorenamekaj.digwallet.mappers;

import com.lorenamekaj.digwallet.dtos.UserDto;
import com.lorenamekaj.digwallet.user.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<User, UserDto> {

    @Override
    public UserDto apply(User user) {
        return new UserDto(
                user.getFullname(),
                user.getEmail(),
                user.getRole()
        );
    }
}
