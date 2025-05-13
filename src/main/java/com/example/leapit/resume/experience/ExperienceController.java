package com.example.leapit.resume.experience;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ExperienceController {
    private final ExperienceService experienceService;
}
