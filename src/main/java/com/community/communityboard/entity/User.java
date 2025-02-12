package com.community.communityboard.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * User 엔티티 클래스
 * - 이 클래스는 MySQL의 "users" 테이블과 매핑됨
 * - 회원 정보를 저장하는 역할
 */
@Entity  // JPA 엔티티로 선언
@Table(name = "users")  // 데이터베이스 테이블명 설정
@Getter  // Lombok: Getter 자동 생성
@Setter  // Lombok: Setter 자동 생성
@NoArgsConstructor  // Lombok: 기본 생성자 자동 생성
@AllArgsConstructor // Lombok: 모든 필드를 포함한 생성자 자동 생성
public class User {

    @Id  // 기본 키(Primary Key) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // MySQL에서 AUTO_INCREMENT 적용
    private Long id;

    @Column(unique = true, nullable = false)  // 중복 불가 + 필수 입력
    private String email;

    @Column(nullable = false)  // 필수 입력
    private String password;

    @Column(nullable = false)  // 필수 입력
    private String nickname;
}
