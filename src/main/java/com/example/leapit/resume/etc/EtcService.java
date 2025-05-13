package com.example.leapit.resume.etc;

import com.example.leapit.resume.Resume;
import com.example.leapit.resume.ResumeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EtcService {
    private final EtcRepository etcRepository;
}
