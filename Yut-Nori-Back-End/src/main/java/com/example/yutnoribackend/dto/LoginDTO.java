package com.example.yutnoribackend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDTO {
    private String userId;
    private String userPassword;
}
