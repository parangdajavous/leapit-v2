package com.example.leapit.resume.training.techstack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TrainingTechStackController {
    private final TrainingTechStackService trainingTechStackService;
}
