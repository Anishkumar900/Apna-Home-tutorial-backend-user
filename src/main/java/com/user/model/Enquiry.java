package com.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Enquiry {

    @Id
    private Long id;
    private String name;
    private String studentClass;
    private String experience;
    private String location;
    private String email;
    private String query;

}
