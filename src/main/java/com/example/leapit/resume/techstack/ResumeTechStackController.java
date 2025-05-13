package com.example.leapit.resume.techstack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ResumeTechStackController {
    private final ResumeTechStackService resumeTechStackService;
}
