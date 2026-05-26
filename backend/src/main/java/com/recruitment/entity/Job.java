package com.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 5000, nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String location;
    
    @Enumerated(EnumType.STRING)
    private JobType jobType;
    
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    
    private String salaryRange;
    
    private Integer minExperience;
    
    private Integer maxExperience;
    
    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private RecruiterProfile recruiter;
    
    @ManyToMany
    @JoinTable(
        name = "job_skills",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> requiredSkills = new HashSet<>();
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications = new HashSet<>();
    
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime postedAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime closingDate;
    
    public enum JobType {
        FULL_TIME,
        PART_TIME,
        CONTRACT,
        INTERNSHIP,
        REMOTE
    }
    
    public enum ExperienceLevel {
        ENTRY_LEVEL,
        MID_LEVEL,
        SENIOR_LEVEL,
        LEAD,
        EXECUTIVE
    }
    
    public enum JobStatus {
        ACTIVE,
        CLOSED,
        DRAFT
    }
}
