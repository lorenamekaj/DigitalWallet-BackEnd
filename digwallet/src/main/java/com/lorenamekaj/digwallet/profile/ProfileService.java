package com.lorenamekaj.digwallet.profile;

import com.lorenamekaj.digwallet.exceptions.DuplicateResourceException;
import com.lorenamekaj.digwallet.exceptions.ResourceNotFoundException;
import com.lorenamekaj.digwallet.user.User;
import com.lorenamekaj.digwallet.user.UserRepository;
import com.lorenamekaj.digwallet.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Transactional
    public Profile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Profile not found for this user!"));
    }

    @Transactional
    public void addProfile(User user) {
        if (profileRepository.existsByUserId(user.getId())) {
            throw new DuplicateResourceException("Profile already exists for user with id" + user.getId());
        }
        if (!userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceNotFoundException("User with email: " + user.getEmail() + " does not exist!");
        }
        Profile profile = new Profile(
                user, 50.0
        );
        profileRepository.save(profile);
    }

    // This is for adding money
    @Transactional
    public void debitProfile(Long userId, double value) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found"));
        Profile profile = getProfileByUserId(user.getId());
        double balance = profile.getBalance();
        if (value < 0) {
            throw new RuntimeException("Balance has to be positive!");
        }
        balance += value;
        profile.setBalance(balance);
        profileRepository.save(profile);
    }

    // This is for removing money
    @Transactional
    public void creditProfile(Long userId, double value) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found"));        Profile profile = getProfileByUserId(user.getId());
        double balance = profile.getBalance();
        if (value < 0) {
            throw new RuntimeException("Balance has to be positive!");
        }
        else if (balance < value) {
            throw new RuntimeException("Balance is too low!");
        }
        balance -= value;
        profile.setBalance(balance);
        profileRepository.save(profile);
    }

}
