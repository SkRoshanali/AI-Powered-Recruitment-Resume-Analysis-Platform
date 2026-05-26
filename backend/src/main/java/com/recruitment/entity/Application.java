package com.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    
    @Column(length = 2000)
    private String coverLetter;
    
    private Double matchScore;
    
    private Double atsScore;
    
    private Integer rankPosition;
    
    @Column(length = 1000)
    private String missingSkills;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime appliedAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum ApplicationStatus {
        APPLIED,
        UNDER_REVIEW,
        SHORTLISTED,
        INTERVIEW_SCHEDULED,
        REJECTED,
        ACCEPTED
    }
}
