package com.example.leapit.jobposting;

import com.example.leapit.application.Application;
import com.example.leapit.common.enums.CareerLevel;
import com.example.leapit.common.enums.EducationLevel;
import com.example.leapit.jobposting.bookmark.JobPostingBookmark;
import com.example.leapit.jobposting.techstack.JobPostingTechStack;
import com.example.leapit.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "job_posting_tb")
@Entity
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String positionType;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private CareerLevel minCareerLevel;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private CareerLevel maxCareerLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EducationLevel educationLevel;

    private Integer addressRegionId;
    private Integer addressSubRegionId;
    private String addressDetail;

    @Lob
    private String serviceIntro;

    @Column(nullable = false)
    private LocalDate deadline;

    @Lob
    @Column(nullable = false)
    private String responsibility;

    @Lob
    @Column(nullable = false)
    private String qualification;

    @Lob
    private String preference;

    @Lob
    private String benefit;

    @Lob
    private String additionalInfo;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer viewCount = 0;

    @CreationTimestamp
    private Timestamp createdAt;

    // 채용 공고 삭제할 때 bookmark에서 참조하고 있어서 같이 지워야 하기 때문에 생성함
    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<JobPostingBookmark> bookmarks;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobPostingTechStack> jobPostingTechStacks;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Application> applications;

    @Builder
    public JobPosting(Integer id, User user, String title, String positionType,
                      CareerLevel minCareerLevel, CareerLevel maxCareerLevel,
                      EducationLevel educationLevel, Integer addressRegionId, Integer addressSubRegionId,
                      String addressDetail, String serviceIntro, LocalDate deadline,
                      String responsibility, String qualification, String preference,
                      String benefit, String additionalInfo) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.positionType = positionType;
        this.minCareerLevel = minCareerLevel;
        this.maxCareerLevel = maxCareerLevel;
        this.educationLevel = educationLevel;
        this.addressRegionId = addressRegionId;
        this.addressSubRegionId = addressSubRegionId;
        this.addressDetail = addressDetail;
        this.serviceIntro = serviceIntro;
        this.deadline = deadline;
        this.responsibility = responsibility;
        this.qualification = qualification;
        this.preference = preference;
        this.benefit = benefit;
        this.additionalInfo = additionalInfo;
        this.viewCount = 0;
        this.jobPostingTechStacks = new ArrayList<>();
    }

    public void update(JobPostingRequest.UpdateDTO dto) {
        this.title = dto.getTitle();
        this.positionType = dto.getPositionType();
        this.minCareerLevel = dto.getMinCareerLevel();
        this.maxCareerLevel = dto.getMaxCareerLevel();
        this.educationLevel = dto.getEducationLevel();
        this.addressRegionId = dto.getAddressRegionId();
        this.addressSubRegionId = dto.getAddressSubRegionId();
        this.addressDetail = dto.getAddressDetail();
        this.serviceIntro = dto.getServiceIntro();
        this.deadline = dto.getDeadline();
        this.responsibility = dto.getResponsibility();
        this.qualification = dto.getQualification();
        this.preference = dto.getPreference();
        this.benefit = dto.getBenefit();
        this.additionalInfo = dto.getAdditionalInfo();
        this.jobPostingTechStacks.clear();
        dto.getTechStackCodes().forEach(code ->
                this.jobPostingTechStacks.add(new JobPostingTechStack(null, this, code))
        );
    }
}