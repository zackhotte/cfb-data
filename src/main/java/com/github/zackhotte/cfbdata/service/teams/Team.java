package com.github.zackhotte.cfbdata.service.teams;

import lombok.Data;

@Data
public class Team {

    private String school; // ESPN API Location
    private String name;
    private String nickname;
    private String displayName;
    private String shortDisplayName;
    private String color;
    private String alternateColor;
    private boolean active;
    private long espnId;

    private String conference;
    private String division;
    private String logo;
    private String logoDark;

    @Override
    public String toString() {
        return displayName;
    }

}
