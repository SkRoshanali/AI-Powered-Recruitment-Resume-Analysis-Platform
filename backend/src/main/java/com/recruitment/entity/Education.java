package com.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "educations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Education {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private JobSeekerProfile jobSeekerProfile;
    
    @Column(nullable = false)
    private String institution;
    
    @Column(nullable = false)
    private String degree;
    
    private String fieldOfStudy;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private Double gpa;
    
    @Column(length = 1000)
    private String description;
}
