package com.example.leapit.resume.link;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LinkController {
    private final LinkService linkService;
}
