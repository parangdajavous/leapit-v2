package com.example.leapit.like;

import com.example.leapit.board.like.LikeRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(LikeRepository.class)
@DataJpaTest
public class LikeRepositoryTest {
}
