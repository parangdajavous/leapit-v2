package com.example.leapit.resume.education;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EducationController {
    private final EducationService educationService;
}
