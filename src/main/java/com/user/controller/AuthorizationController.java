package com.user.controller;

import com.user.model.Registration;
import com.user.request.ForgetPassword;
import com.user.request.Login;
import com.user.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/api/v1")
@RequiredArgsConstructor
public class AuthorizationController{

    private final AuthorizationService authorizationService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody Registration registration){
        return new ResponseEntity<>(authorizationService.registration(registration), HttpStatus.OK);
    }

    @PostMapping("/registration-otp-verification")
    public ResponseEntity<String> registrationSuccessful(@RequestBody Registration registration){
        return new ResponseEntity<>(authorizationService.registrationSuccessful(registration),HttpStatus.CREATED);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPassword forgetPassword){
        return new ResponseEntity<>(authorizationService.forgetPassword(forgetPassword),HttpStatus.OK);
    }

    @PostMapping("/forgot-password-verify")
    public ResponseEntity<String> verifyForgetPassword(@RequestBody ForgetPassword forgetPassword){
        return new ResponseEntity<>(authorizationService.verifyForgetPassword(forgetPassword),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login){
        return new ResponseEntity<>(authorizationService.login(login),HttpStatus.OK);
    }



}
