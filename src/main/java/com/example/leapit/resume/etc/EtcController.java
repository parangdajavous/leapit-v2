package com.example.leapit.resume.etc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EtcController {
    private final EtcService etcService;
}
