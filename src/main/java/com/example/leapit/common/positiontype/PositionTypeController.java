package com.example.leapit.common.positiontype;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PositionTypeController {
    private final PositionTypeService positionTypeService;
}
