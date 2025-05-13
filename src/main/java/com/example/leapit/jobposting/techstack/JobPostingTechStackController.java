package com.example.leapit.jobposting.techstack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class JobPostingTechStackController {
    private final JobPostingTechStackService jobPostingTechStackService;
}
