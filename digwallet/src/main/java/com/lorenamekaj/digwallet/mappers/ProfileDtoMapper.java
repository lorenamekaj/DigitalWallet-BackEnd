package com.lorenamekaj.digwallet.mappers;

import com.lorenamekaj.digwallet.dtos.ProfileDto;
import com.lorenamekaj.digwallet.profile.Profile;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProfileDtoMapper implements Function<Profile, ProfileDto> {
    @Override
    public ProfileDto apply(Profile profile) {
        return new ProfileDto(
                profile.getId(),
                profile.getBalance()
        );
    }
}
