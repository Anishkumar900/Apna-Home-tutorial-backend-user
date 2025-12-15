package com.user.serviceImpl;

import com.user.model.Enquiry;
import com.user.repository.EnquiryRepository;
import com.user.response.ApiResponse;
import com.user.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnquiryServiceImpl implements EnquiryService {
    private final EnquiryRepository enquiryRepository;

    @Override
    public ApiResponse createEnquiry(Enquiry enquiry) {
        enquiryRepository.save(enquiry);
        return new ApiResponse("Create", HttpStatus.CREATED.value());
    }
}
