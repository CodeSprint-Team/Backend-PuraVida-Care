package com.cenfotec.backendcodesprint.logic.Admin.Mapper;

import com.cenfotec.backendcodesprint.logic.Admin.DTO.UserStatusDTO;
import com.cenfotec.backendcodesprint.logic.Model.User;
import org.springframework.stereotype.Component;

@Component
public class AdminUserMapper {

    public UserStatusDTO toDTO(User u) {
        UserStatusDTO dto = new UserStatusDTO();
        dto.setUserId(u.getId());
        dto.setFullName(u.getUserName() + " " + u.getLastName());
        dto.setEmail(u.getEmail());
        dto.setUserState(u.getUserState());
        dto.setProvider(u.getProvider());
        dto.setPhotoUrl(u.getPhotoUrl());
        dto.setRole(u.getRole() != null ? u.getRole().getRoleName() : "N/A");
        return dto;
    }
}