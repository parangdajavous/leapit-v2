package com.example.leapit.user;


import com.example.leapit.common.enums.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;

@Import(UserRepository.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername_test() {
        String username = "ssar";
        Optional<User> userOP = userRepository.findByUsername(username);
        System.out.println("===========유저네임중복체크============");
        System.out.println(userOP.get().getUsername());
        System.out.println(userOP.get().getPassword());
        System.out.println("===========유저네임중복체크============");
    }

    @Test
    public void save_test() {
        // given
        String name = "가나다";
        String username = "haha";
        String password = "1234QWER!@#$qwer";
        String email = "haha@gmail.com";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        String contactNumber = "010-9999-1111";
        Role role = Role.PERSONAL;
        User user = User.builder()
                .name(name)
                .username(username)
                .password(password)
                .email(email)
                .birthDate(birthday)
                .contactNumber(contactNumber)
                .role(role)
                .build();

        // when
        User savedUser = userRepository.save(user);

        // eye
        System.out.println("============== 유저 저장 결과 ==============");
        System.out.println("ID: " + savedUser.getId());
        System.out.println("이름: " + savedUser.getName());
        System.out.println("아이디: " + savedUser.getUsername());
        System.out.println("비밀번호: " + savedUser.getPassword());
        System.out.println("이메일: " + savedUser.getEmail());
        System.out.println("생년월일: " + savedUser.getBirthDate());
        System.out.println("연락처: " + savedUser.getContactNumber());
        System.out.println("권한: " + savedUser.getRole());
        System.out.println("============================================");
    }

    @Test
    public void findById_test() {
        // given
        Integer userId = 7;

        // when
        Optional<User> userOP = userRepository.findById(userId);

        // eye
        System.out.println("========== 유저 조회 결과 ==========");
        if (userOP.isPresent()) {
            System.out.println("ID: " + userOP.get().getId());
            System.out.println("이름: " + userOP.get().getName());
            System.out.println("아이디: " + userOP.get().getUsername());
            System.out.println("이메일: " + userOP.get().getEmail());
            System.out.println("전화번호: " + userOP.get().getContactNumber());
            System.out.println("역할: " + userOP.get().getRole());
            System.out.println("생년월일: " + userOP.get().getBirthDate());
        } else {
            System.out.println("해당 ID의 유저가 존재하지 않습니다.");
        }
        System.out.println("===================================");
    }
}
