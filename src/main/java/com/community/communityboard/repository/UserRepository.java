package com.community.communityboard.repository;

import com.community.communityboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * UserRepository 인터페이스
 * - JPA를 사용하여 MySQL과 상호작용
 * - 기본적인 CRUD 기능을 자동으로 제공함
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일을 이용해 사용자 정보를 조회하는 메서드
     * - 로그인 시 사용됨
     */
    Optional<User> findByEmail(String email);

    /**
     * 이메일이 이미 존재하는지 확인하는 메서드
     * - 회원가입 시 중복 검사에 사용됨
     */
    boolean existsByEmail(String email);
}
