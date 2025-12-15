package com.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> testAPI(){
        return new ResponseEntity<>("Successfully", HttpStatus.OK);
    }
}
