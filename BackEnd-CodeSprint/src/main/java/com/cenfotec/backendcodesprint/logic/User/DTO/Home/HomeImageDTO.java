package com.cenfotec.backendcodesprint.logic.User.DTO.Home;

import lombok.Data;

@Data
public class HomeImageDTO {
    private Long homeId;
    private String room;
    private String type;
    private String imageBase64;
}
