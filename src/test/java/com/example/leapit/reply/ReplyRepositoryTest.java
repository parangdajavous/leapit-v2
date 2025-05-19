package com.example.leapit.reply;

import com.example.leapit.board.reply.ReplyRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(ReplyRepository.class)
@DataJpaTest
public class ReplyRepositoryTest {
}
