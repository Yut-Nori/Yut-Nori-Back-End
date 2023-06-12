package com.example.yutnoribackend.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data // @Getter, @Setter, @ToString, @EqialsAndHashCode, @RequiredArgsConstructor 모두 설정 어노테이션
public class SignupDTO {
    @NotNull // Controller에서 @valid시 null 값 허용 안함
    private String userId;
    @NotNull
    private String userPassword;
    @NotNull
    private String userPasswordVarification;
    @NotNull
    private String userNickname;

    @Email // Controller에서 @valid시 이메일의 형식인지 검증
    private String userEmail;
}
