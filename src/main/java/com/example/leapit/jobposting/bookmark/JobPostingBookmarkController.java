package com.example.leapit.jobposting.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class JobPostingBookmarkController {
    private final JobPostingBookmarkService jobPostingBookmarkService;
}