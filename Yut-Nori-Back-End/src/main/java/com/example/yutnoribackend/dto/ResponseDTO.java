package com.example.yutnoribackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data // @Getter, @Setter, @ToString, @EqialsAndHashCode, @RequiredArgsConstructor 모두 설정 어노테이션
public class ResponseDTO {
    @NonNull
    private int statusCode;
    @NonNull
    private String message;
}
