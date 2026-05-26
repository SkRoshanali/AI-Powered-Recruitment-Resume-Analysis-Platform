package com.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "certifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private JobSeekerProfile jobSeekerProfile;
    
    @Column(nullable = false)
    private String name;
    
    private String issuingOrganization;
    
    private LocalDate issueDate;
    
    private LocalDate expiryDate;
    
    private String credentialId;
    
    private String credentialUrl;
}
