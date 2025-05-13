package com.example.leapit.resume.project.techstack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectTechStackController {
    private final ProjectTechStackService projectTechStackService;
}
