package com.quickhr.entity;

import com.quickhr.enums.user.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_employee")
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;

    private Long companyId;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;
    
    @Column(unique = true, length = 25)
    private String identityNumber;
    
    private LocalDate dateOfBirth;
    
    @Column(unique = true, length = 25)
    private String phone;
    
    @Column(length = 300)
    private String address;
    
    @Enumerated(EnumType.STRING)
    private EGender gender;
    
    @Enumerated(EnumType.STRING)
    private EMaritalStatus maritalStatus;
    
    @Column(unique = true, length = 50)
    private String mail;
    
    @Enumerated(EnumType.STRING)
    EEducationLevel educationLevel;
    
    private String position;
    
    @Enumerated(EnumType.STRING)
    EWorkType workType;
    
    private LocalDate dateOfEmployment;
    
    private LocalDate dateOfTermination;
    
    private Double salary;

    @Enumerated(EnumType.STRING)
    private EEmploymentStatus employmentStatus;

    @Enumerated(EnumType.STRING)
    private EUserState userState;
}