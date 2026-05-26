package com.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String jobType;
    private String experienceLevel;
    private String salaryRange;
    private Integer minExperience;
    private Integer maxExperience;
    private Set<String> requiredSkills;
    private String companyName;
    private Long recruiterId;
    private String status;
    private LocalDateTime postedAt;
    private LocalDateTime closingDate;
}
