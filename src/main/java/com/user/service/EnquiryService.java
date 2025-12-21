package com.user.service;

import com.user.model.Enquiry;
import com.user.model.StudentParentEnquiry;
import com.user.response.ApiResponse;

public interface EnquiryService {
    String createEnquiry(Enquiry enquiry);
    String createStudentEnquiry(StudentParentEnquiry studentParentEnquiry);
}
