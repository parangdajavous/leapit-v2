package com.example.leapit.postiontype;

import com.example.leapit.common.positiontype.PositionTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(PositionTypeRepository.class)
@DataJpaTest
public class PostionTypeRepositoryTest {

    @Autowired
    private PositionTypeRepository positionTypeRepository;

    @Test
    public void position_type_repository_test() {
        // when
        List<String> result = positionTypeRepository.findAll();

        // eye
        System.out.println("직무 코드 목록 = " + result);
        // 직무 코드 목록 = [AI 엔지니어, 데이터 엔지니어, 모바일 앱 개발자, 백엔드, 풀스택, 프론트엔드]
    }
}