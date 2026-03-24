package com.cenfotec.backendcodesprint.logic.User.Mapper;

import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.User.DTO.Response.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto toResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setId(user.getId());
        responseDto.setUserName(user.getUserName());
        responseDto.setLastName(user.getLastName());
        responseDto.setEmail(user.getEmail());
        responseDto.setUserState(user.getUserState());

        if (user.getRole() != null) {
            responseDto.setRoleId(user.getRole().getRoleName());
        }

        responseDto.setCreated(user.getCreated());
        responseDto.setUpdated(user.getUpdated());

        return responseDto;
    }
}