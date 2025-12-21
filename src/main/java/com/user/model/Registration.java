package com.user.model;

import com.user.dom.ProfileVerification;
import com.user.dom.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String phoneNumber;
    private String studentClass;
    private String experience;
    private String email;
    private String password;
    private String location;
    private String otp;
    private LocalDateTime expireOTPTime;
    @Enumerated(EnumType.STRING)
    private ProfileVerification profileVerification;
    private LocalDateTime localDateTime;



//    private int failedLoginAttempts;
//    private boolean accountLocked;
//    private LocalDateTime lockTime;

}
