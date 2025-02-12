package com.community.communityboard.controller;

import com.community.communityboard.dto.AuthRequest;
import com.community.communityboard.entity.User;
import com.community.communityboard.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 회원가입 API를 처리하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입 API
     * - 이메일 중복 체크 후, 새로운 사용자 저장
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        // 요청 객체가 제대로 들어오는지 로그 찍어보기
        System.out.println("회원가입 요청 데이터: " + request.getEmail() + ", " + request.getPassword() + ", " + request.getNickname());

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화
        user.setNickname(request.getNickname());

        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공!");
    }
}
