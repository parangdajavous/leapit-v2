package com.example.leapit.resume;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ResumeController {
    private final ResumeService resumeService;
}
