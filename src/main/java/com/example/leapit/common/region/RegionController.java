package com.example.leapit.common.region;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RegionController {
    private final RegionService regionService;
}

