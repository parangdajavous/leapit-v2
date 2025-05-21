package com.example.leapit.application.bookmark;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.error.ex.ExceptionApi403;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit.application.Application;
import com.example.leapit.application.ApplicationRepository;
import com.example.leapit.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ApplicationBookmarkService {
    private final ApplicationBookmarkRepository applicationBookmarkRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public ApplicationBookmarkResponse.SaveDTO save(ApplicationBookmarkRequest.SaveDTO reqDTO, User sessionUser) {
        Application application = applicationRepository.findById(reqDTO.getApplicationId())
                .orElseThrow(() -> new ExceptionApi404("존재하지 않는 지원입니다."));

        Optional<ApplicationBookmark> applicationBookmarkOP = applicationBookmarkRepository.findByUserIdAndApplicationId(sessionUser.getId(),reqDTO.getApplicationId());
        if (applicationBookmarkOP.isPresent()) throw new ExceptionApi400("이미 스크랩된 지원입니다.");

                ApplicationBookmark bookmark = ApplicationBookmark.builder()
                .user(sessionUser)
                .application(application)
                .build();

        applicationBookmarkRepository.save(bookmark);
        return new ApplicationBookmarkResponse.SaveDTO(bookmark);
    }

    @Transactional
    public void delete(Integer applicationId, Integer sessionUserId) {
        ApplicationBookmark bookmark = applicationBookmarkRepository.findByUserIdAndApplicationId(sessionUserId, applicationId)
                .orElseThrow(() -> new ExceptionApi404("해당 스크랩이 존재하지 않습니다."));
        if (!bookmark.getUser().getId().equals(sessionUserId)) throw new ExceptionApi403("권한이 없습니다.");

        applicationBookmarkRepository.delete(bookmark);
    }
}
