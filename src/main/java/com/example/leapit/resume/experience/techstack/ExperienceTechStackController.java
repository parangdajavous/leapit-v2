package com.example.leapit.resume.experience.techstack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ExperienceTechStackController {
    private final ExperienceTechStackService experienceTechStackService;
}
