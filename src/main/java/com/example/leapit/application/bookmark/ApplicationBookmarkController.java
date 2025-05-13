package com.example.leapit.application.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ApplicationBookmarkController {
    private final ApplicationBookmarkService bookmarkService;
}
