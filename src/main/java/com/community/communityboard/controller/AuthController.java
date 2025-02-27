package com.community.communityboard.controller;

import com.community.communityboard.dto.AuthRequest;
import com.community.communityboard.entity.User;
import com.community.communityboard.repository.UserRepository;
import com.community.communityboard.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 *  회원가입 & 로그인 API 컨트롤러
 * - 사용자의 회원가입 요청을 처리하고, 로그인 요청을 받아 JWT 토큰을 발급함.
 * - JWT 기반 인증 방식을 사용하여 이후 API 요청에서 인증을 유지함.
 */
@RestController
@RequestMapping("/auth") // 모든 API의 기본 경로는 /auth
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // JWT 토큰 생성을 위한 유틸리티 클래스

    // 생성자 주입 (Spring이 자동으로 필요한 빈을 주입함)
    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     *  회원가입 API
     * - 사용자가 이메일, 비밀번호, 닉네임을 입력하면 회원 정보 저장
     * - 비밀번호는 BCrypt로 암호화하여 저장
     * - 이메일 중복 체크 후, 중복이 있으면 회원가입 불가
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest request) {
        logger.info("회원가입 요청: 이메일={}, 닉네임={}", request.getEmail(), request.getNickname());

        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("이미 존재하는 이메일: {}", request.getEmail());
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }

        // 2. 유저 정보 저장 (비밀번호 암호화 후 저장)
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화
        user.setNickname(request.getNickname());

        userRepository.save(user);

        logger.info("회원가입 완료! 저장된 이메일: {}", user.getEmail());
        return ResponseEntity.ok("회원가입 성공!");
    }

    /**
     *  로그인 API
     * - 사용자가 이메일과 비밀번호를 입력하면 인증을 수행
     * - JWT 토큰을 생성하여 반환
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        logger.info("로그인 요청: 이메일={}", request.getEmail());

        // 1. 사용자가 존재하는지 확인
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            logger.warn("사용자를 찾을 수 없음: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
        }

        // 2. 비밀번호 검증 (입력값과 DB 저장된 해시 비교)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("비밀번호 불일치: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail());

        // 4. 토큰 반환 (JSON 형식)
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
