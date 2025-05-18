package com.example.leapit.jobposting.techstack;

import com.example.leapit.jobposting.JobPosting;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "job_posting_tech_stack_tb")
@Entity
public class JobPostingTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPosting jobPosting;

    @Column(nullable = false)
    private String techStack;

    @Builder
    public JobPostingTechStack(Integer id, JobPosting jobPosting, String techStack) {
        this.id = id;
        this.jobPosting = jobPosting;
        this.techStack = techStack;
    }
}