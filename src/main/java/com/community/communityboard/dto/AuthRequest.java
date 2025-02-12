package com.community.communityboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청을 처리하는 DTO 클래스
 */
@Getter
@Setter
public class AuthRequest {

    @JsonProperty("email")  // JSON에서 "email" 필드와 매핑
    private String email;

    @JsonProperty("password")  // JSON에서 "password" 필드와 매핑
    private String password;

    @JsonProperty("nickname")  // JSON에서 "nickname" 필드와 매핑
    private String nickname;
}
