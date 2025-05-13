package com.example.leapit.jobposting;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class JobPostingController {
    private final JobPostingService jobPostingService;
}