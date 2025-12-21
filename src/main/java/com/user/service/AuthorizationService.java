package com.user.service;

import com.user.model.Registration;
import com.user.request.ForgetPassword;
import com.user.request.Login;

public interface AuthorizationService {
    String registration(Registration registration);
    String registrationSuccessful(Registration registration);
    String forgetPassword(ForgetPassword forgetPassword);
    String verifyForgetPassword(ForgetPassword forgetPassword);
    String login(Login login);
}
