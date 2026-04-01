package com.cenfotec.backendcodesprint.logic.User.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String userName;
    private String lastName;
    private String email;
    private String roleId;
    private String userState;
    private LocalDateTime created;
    private LocalDateTime updated;
}