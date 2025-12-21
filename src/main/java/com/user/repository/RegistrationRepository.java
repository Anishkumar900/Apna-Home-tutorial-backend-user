package com.user.repository;

import com.user.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    Registration findByEmail(String email);
    Registration findByPhoneNumber(String phoneNumber);

}
