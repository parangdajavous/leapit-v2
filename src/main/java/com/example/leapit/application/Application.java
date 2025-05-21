package com.example.leapit.application;

import com.example.leapit.common.enums.BookmarkStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookmarkStatus bookmark;

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

    @Builder
    public Application(Integer id, Resume resume, JobPosting jobPosting, BookmarkStatus bookmark, LocalDate appliedDate, PassStatus passStatus, ViewStatus viewStatus) {
        this.id = id;
        this.resume = resume;
        this.jobPosting = jobPosting;
        this.bookmark = bookmark;
        this.appliedDate = appliedDate;
        this.passStatus = passStatus;
        this.viewStatus = viewStatus;
    }


    public void updateBookmark(String bookmark) {
        this.bookmark = bookmark == "BOOKMARKED"? BookmarkStatus.NOT_BOOKMARKED : BookmarkStatus.BOOKMARKED;
    }
}
