package com.example.leapit.techstack;

import com.example.leapit.common.techstack.TechStackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(TechStackRepository.class)
@DataJpaTest
public class TechStackRepositoryTest {

    @Autowired
    private TechStackRepository techStackRepository;

    @Test
    public void tech_stack_repository_test() {
        // when
        List<String> result = techStackRepository.findAll();

        // eye
        System.out.println("기술 스택 목록 = " + result);
        // 기술 스택 목록 = [CSS, Django, HTML, Java, Kotlin, Node.js, Python, React, SQL, Spring Boot]
    }
}