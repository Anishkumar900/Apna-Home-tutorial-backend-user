package com.user.service;

import com.user.model.FeedbackProfile;
import com.user.model.Registration;

public interface ProfileService {
    Registration getProfile(String token);
    String addFeedback(FeedbackProfile feedbackProfile);
}
