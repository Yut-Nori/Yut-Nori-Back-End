package com.example.yutnoribackend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data // @Getter, @Setter, @ToString, @EqialsAndHashCode, @RequiredArgsConstructor 모두 설정 어노테이션
public class TokenDTO {
    private String grantType; // JWT 인증 타입
    private String accessToken;
    private String refreshToken;
}
