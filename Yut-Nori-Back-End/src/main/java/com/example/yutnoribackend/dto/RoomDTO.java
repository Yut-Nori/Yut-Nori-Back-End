package com.example.yutnoribackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;


@Builder
@Data
public class RoomDTO {
    @NonNull
    private String roomName;
    @NonNull
    private String roomPassword;

    private boolean roomVisible;
}
