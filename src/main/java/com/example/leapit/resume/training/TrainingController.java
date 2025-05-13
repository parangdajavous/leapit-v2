package com.example.leapit.resume.training;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TrainingController {
    private final TrainingService trainingService;
}
