package com.user.controller;

import com.user.model.Registration;
import jakarta.ws.rs.HeaderParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api/auth/v1")
@RequiredArgsConstructor
public class ProfileUserController {

//    @GetMapping("/login")
//    public ResponseEntity<Registration> tokenVerify(@HeaderParam("token") token){
//
//    }


}
