package com.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {
    private Long id;
    private Long userId;
    private String applicantName;
    private String applicantEmail;
    private Long jobId;
    private String jobTitle;
    private String status;
    private String coverLetter;
    private Double matchScore;
    private Double atsScore;
    private Integer rankPosition;
    private String missingSkills;
    private LocalDateTime appliedAt;
}
