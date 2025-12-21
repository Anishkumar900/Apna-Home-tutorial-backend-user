package com.user.model;

import com.user.dom.VerifyEnquiry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StudentParentEnquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String studentQuery;
    @Enumerated(EnumType.STRING)
    private VerifyEnquiry verifyEnquiry;
    private LocalDateTime localDateTime;
}
