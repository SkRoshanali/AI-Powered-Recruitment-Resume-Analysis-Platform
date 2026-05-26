package com.recruitment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysisResponse {
    private List<String> skills;
    private List<String> education;
    private List<String> experience;
    private List<String> certifications;
    private Double atsScore;
    private Map<String, Object> metadata;
    private String summary;
}
