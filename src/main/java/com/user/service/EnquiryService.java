package com.user.service;

import com.user.model.Enquiry;
import com.user.response.ApiResponse;

public interface EnquiryService {
    ApiResponse createEnquiry(Enquiry enquiry);
}
