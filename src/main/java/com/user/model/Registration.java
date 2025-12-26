package com.user.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.user.dom.Payment;
import com.user.dom.ProfileVerification;
import com.user.dom.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(unique = true, nullable = false)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String location;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String otp;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime expireOTPTime;
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProfileVerification profileVerification;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime localDateTime;
    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Payment payment;
    private LocalDateTime paymentValidTill;

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<FeedbackProfile> feedbackProfileList = new ArrayList<>();



}





//    private int failedLoginAttempts;
//    private boolean accountLocked;
//    private LocalDateTime lockTime;