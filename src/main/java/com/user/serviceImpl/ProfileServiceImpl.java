package com.user.serviceImpl;

import com.user.config.JWTService;
import com.user.model.FeedbackProfile;
import com.user.model.Registration;
import com.user.repository.RegistrationRepository;
import com.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final RegistrationRepository registrationRepository;
    private final JWTService jwtService;

    @Override
    public Registration getProfile(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization token");
        }

        String jwtToken = token.substring(7);
        String email = jwtService.extractUsername(jwtToken);

        return registrationRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );
    }


    @Override
    public String addFeedback(FeedbackProfile feedbackProfile) {

        Registration registration = registrationRepository
                .findByEmail(feedbackProfile.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found!")
                );

        // Set parent reference (IMPORTANT for JPA)
        feedbackProfile.setProfile(registration);

        // Add feedback to list
        registration.getFeedbackProfileList().add(feedbackProfile);

        // Save parent (child will auto-save because of CascadeType.ALL)
        registrationRepository.save(registration);

        return "Feedback added successfully!";
    }


}
