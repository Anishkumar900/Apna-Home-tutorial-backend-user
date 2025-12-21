package com.user.repository;

import com.user.model.StudentParentEnquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentParentRepository extends JpaRepository<StudentParentEnquiry,Long> {
}
