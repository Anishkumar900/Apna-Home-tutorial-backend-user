package com.user.repository;

import com.user.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Optional<Registration> findByEmail(String email);

    Optional<Registration> findByPhoneNumber(String phoneNumber);
}

