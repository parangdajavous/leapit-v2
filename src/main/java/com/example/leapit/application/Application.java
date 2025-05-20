package com.example.leapit.application;

import com.example.leapit.application.bookmark.ApplicationBookmark;
import com.example.leapit.common.enums.PassStatus;
import com.example.leapit.common.enums.ViewStatus;
import com.example.leapit.jobposting.JobPosting;
import com.example.leapit.resume.Resume;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "application_tb")
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    @OneToMany(mappedBy = "application", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ApplicationBookmark> applicationBookmarks;

    private LocalDate appliedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassStatus passStatus; // PASS / FAIL / WAITING

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ViewStatus viewStatus; // VIEWED / UNVIEWED

    public void updatePassStatus(PassStatus passStatus) {
        this.passStatus = passStatus;
    }

    public void updateViewStatus(ViewStatus viewStatus) {
        this.viewStatus = viewStatus;
    }
}