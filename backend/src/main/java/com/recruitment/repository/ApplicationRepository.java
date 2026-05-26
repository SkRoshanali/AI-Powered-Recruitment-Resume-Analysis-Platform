package com.recruitment.repository;

import com.recruitment.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    Page<Application> findByUserId(Long userId, Pageable pageable);
    
    Page<Application> findByJobId(Long jobId, Pageable pageable);
    
    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId ORDER BY a.matchScore DESC, a.atsScore DESC")
    List<Application> findRankedCandidatesByJobId(@Param("jobId") Long jobId);
    
    Optional<Application> findByUserIdAndJobId(Long userId, Long jobId);
    
    Boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
