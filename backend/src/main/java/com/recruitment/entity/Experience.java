package com.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "experiences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private JobSeekerProfile jobSeekerProfile;
    
    @Column(nullable = false)
    private String company;
    
    @Column(nullable = false)
    private String jobTitle;
    
    private String location;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private Boolean currentlyWorking = false;
    
    @Column(length = 2000)
    private String description;
}
