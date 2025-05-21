package com.example.leapit.user;

import com.example.leapit._core.error.ex.ExceptionApi400;
import com.example.leapit._core.error.ex.ExceptionApi401;
import com.example.leapit._core.error.ex.ExceptionApi404;
import com.example.leapit._core.util.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    // 개인 회원가입
    @Transactional
    public UserResponse.DTO join(UserRequest.@Valid PersonalJoinDTO reqDTO) {
        String encPassword = BCrypt.hashpw(reqDTO.getPassword(), BCrypt.gensalt());
        reqDTO.setPassword(encPassword);

        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());
        if (userOP.isPresent()) throw new ExceptionApi400("중복된 유저네임이 존재합니다");

        User userPS = userRepository.save(reqDTO.toEntity());
        return new UserResponse.DTO(userPS);
    }

    // 기업 회원가입
    @Transactional
    public UserResponse.DTO join(UserRequest.@Valid CompanyJoinDTO reqDTO) {
        String encPassword = BCrypt.hashpw(reqDTO.getPassword(), BCrypt.gensalt());
        reqDTO.setPassword(encPassword);

        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());
        if (userOP.isPresent()) throw new ExceptionApi400("중복된 유저네임이 존재합니다");

        User userPS = userRepository.save(reqDTO.toEntity());
        return new UserResponse.DTO(userPS);
    }

    // 유저네임 중복체크
    public Map<String, Object> checkUsernameAvailable(String username) {
        Optional<User> userOP = userRepository.findByUsername(username);
        Map<String, Object> respDTO = new HashMap<>();

        if (userOP.isPresent()) {
            respDTO.put("available", false);
        } else {
            respDTO.put("available", true);
        }
        return respDTO;
    }


    // 로그인
    public UserResponse.TokenDTO login(UserRequest.@Valid LoginDTO loginDTO) {
        User userPS = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new ExceptionApi401("유저네임 혹은 비밀번호가 틀렸습니다"));
        if (userPS.getRole() != loginDTO.getRole()) throw new ExceptionApi401("유저네임 혹은 비밀번호가 틀렸습니다");

        Boolean isSame = BCrypt.checkpw(loginDTO.getPassword(), userPS.getPassword());
        if (!isSame) throw new ExceptionApi401("유저네임 혹은 비밀번호가 틀렸습니다");

        // 토큰 생성
        String accessToken = JwtUtil.create(userPS);
        return UserResponse.TokenDTO.builder().accessToken(accessToken).build();
    }

    // 기업 유저 정보 수정
    @Transactional
    public UserResponse.UpdateDTO update(UserRequest.CompanyUpdateDTO reqDTO, Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        if (reqDTO.getContactNumber()==null||reqDTO.getContactNumber().isBlank()) {reqDTO.setContactNumber(userPS.getContactNumber());}
        if (reqDTO.getNewPassword()==null||reqDTO.getNewPassword().isBlank()) {reqDTO.setNewPassword(userPS.getPassword());}

        userPS.companyUpdate(reqDTO);
        return new UserResponse.UpdateDTO(userPS);
    }

    // 개인 유저 정보 수정
    @Transactional
    public UserResponse.UpdateDTO update(UserRequest.PersonalUpdateDTO reqDTO, Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        if (reqDTO.getContactNumber()==null||reqDTO.getContactNumber().isBlank()) {reqDTO.setContactNumber(userPS.getContactNumber());}
        if (reqDTO.getNewPassword()==null||reqDTO.getNewPassword().isBlank()) {reqDTO.setNewPassword(userPS.getPassword());}
        userPS.personalUpdate(reqDTO);

        return new UserResponse.UpdateDTO(userPS);
    }

    // 기업 유저 정보 수정 화면
    public UserResponse.UpdateDTO getPersonalUpdateForm(Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));
        UserResponse.UpdateDTO respDTO = new UserResponse.UpdateDTO(userPS);
        return respDTO;
    }

    // 기업 유저 정보 수정 화면
    public UserResponse.UpdateDTO getCompanyUpdateForm(Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));
        UserResponse.UpdateDTO respDTO = new UserResponse.UpdateDTO(userPS);
        return respDTO;
    }
}