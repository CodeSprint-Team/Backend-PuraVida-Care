package com.cenfotec.backendcodesprint.logic.User.DTO.Home;

import lombok.Data;

@Data
public class HomeMarkerDTO {
    private Long id;
    private String type;
    private String title;
    private String description;
    private String contact;
    private String status;
    private Integer positionX;
    private Integer positionY;
    private String icon;
    private String room;
}