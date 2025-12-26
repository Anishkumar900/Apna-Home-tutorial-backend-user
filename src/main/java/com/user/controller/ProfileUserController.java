package com.user.controller;

import com.user.model.FeedbackProfile;
import com.user.model.Registration;
import com.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/api/auth/v1")
@RequiredArgsConstructor
public class ProfileUserController {

    private final ProfileService profileService;

    @GetMapping("/jwt-verify")
    public ResponseEntity<Registration> tokenVerify(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(profileService.getProfile(token));
    }

    @PostMapping("/feedback")
    public ResponseEntity<String> feedback(@RequestHeader("Authorization") String token, @RequestBody FeedbackProfile feedbackProfile){
        return ResponseEntity.ok(profileService.addFeedback(feedbackProfile));
    }



}
