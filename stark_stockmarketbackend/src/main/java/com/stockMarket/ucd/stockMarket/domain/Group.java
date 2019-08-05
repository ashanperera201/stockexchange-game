package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

public class Group {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "Group Name")
    private String groupName;

    @ApiModelProperty(notes = "user Id")
    private String userId;

    @ApiModelProperty(notes = "No of players")
    private int players;

    @ApiModelProperty(notes = "User Name")
    private String userName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
