package com.lorenamekaj.digwallet.profile;

import com.lorenamekaj.digwallet.dtos.ProfileDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<Profile> getProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("my-profile")
    public ProfileDto getProfile() {
        return profileService.getAuthenticatedUserProfile();
    }

}
