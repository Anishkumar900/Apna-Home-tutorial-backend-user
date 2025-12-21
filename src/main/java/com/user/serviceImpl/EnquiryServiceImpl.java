package com.user.serviceImpl;

import com.user.dom.VerifyEnquiry;
import com.user.dto.WordFormation;
import com.user.model.Enquiry;
import com.user.model.StudentParentEnquiry;
import com.user.repository.EnquiryRepository;
import com.user.repository.StudentParentRepository;
import com.user.response.ApiResponse;
import com.user.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnquiryServiceImpl implements EnquiryService {
    private final EnquiryRepository enquiryRepository;
    private final StudentParentRepository studentParentRepository;

    @Override
    public String createEnquiry(Enquiry enquiry) {
        enquiry.setName(WordFormation.nameFormation(enquiry.getName()));
        enquiry.setEmail(WordFormation.emailFormation(enquiry.getEmail()));
        enquiry.setVerifyEnquiry(VerifyEnquiry.PENDING_ENQUIRY);
        enquiry.setLocalDateTime(LocalDateTime.now());
        enquiryRepository.save(enquiry);
        return "Enquiry created successfully";
    }

    @Override
    public String createStudentEnquiry(StudentParentEnquiry studentParentEnquiry) {
        studentParentEnquiry.setName(WordFormation.nameFormation(studentParentEnquiry.getName()));
        studentParentEnquiry.setEmail(WordFormation.emailFormation(studentParentEnquiry.getEmail()));
        studentParentEnquiry.setVerifyEnquiry(VerifyEnquiry.PENDING_ENQUIRY);
        studentParentEnquiry.setLocalDateTime(LocalDateTime.now());
        studentParentRepository.save(studentParentEnquiry);
        return "Student parent enquiry created successfully!";
    }
}
