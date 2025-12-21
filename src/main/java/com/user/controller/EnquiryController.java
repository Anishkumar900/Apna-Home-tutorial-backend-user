package com.user.controller;

import com.user.model.Enquiry;
import com.user.model.StudentParentEnquiry;
import com.user.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/api/v1")
@RequiredArgsConstructor
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PostMapping("/enquiry")
    public ResponseEntity<String> enquiry(@RequestBody Enquiry enquiry){
        return new ResponseEntity<>(enquiryService.createEnquiry(enquiry), HttpStatus.CREATED);
    }

    @PostMapping("/student-query")
    public ResponseEntity<String> studentEnquiry(@RequestBody StudentParentEnquiry studentParentEnquiry){
        return new ResponseEntity<>(enquiryService.createStudentEnquiry(studentParentEnquiry),HttpStatus.CREATED);
    }
}
