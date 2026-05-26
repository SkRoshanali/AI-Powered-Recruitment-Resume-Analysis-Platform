package com.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recruiter_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String companyName;
    
    private String companyWebsite;
    
    @Column(length = 2000)
    private String companyDescription;
    
    private String industry;
    
    private String companySize;
    
    private String location;
    
    private Boolean isVerified = false;
    
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Job> jobs = new HashSet<>();
}
