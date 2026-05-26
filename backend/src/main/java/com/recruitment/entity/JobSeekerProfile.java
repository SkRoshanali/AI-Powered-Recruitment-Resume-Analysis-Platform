package com.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_seeker_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(length = 2000)
    private String summary;
    
    private String location;
    
    private Integer experienceYears;
    
    private String currentJobTitle;
    
    private String resumePath;
    
    private Double atsScore;
    
    @ElementCollection
    @CollectionTable(name = "job_seeker_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill")
    private Set<String> skills = new HashSet<>();
    
    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Education> educations = new HashSet<>();
    
    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Experience> experiences = new HashSet<>();
    
    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Certification> certifications = new HashSet<>();
}
