package com.user.repository;

import com.user.model.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnquiryRepository extends JpaRepository<Enquiry,Long> {
}
