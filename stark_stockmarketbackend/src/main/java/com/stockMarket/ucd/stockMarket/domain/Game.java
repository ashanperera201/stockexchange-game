package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

public class Game {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "Group Name")
    private String groupName;

    @ApiModelProperty(notes = "user Id")
    private String userId;

    @ApiModelProperty(notes = "Round No")
    private int roundNo;

    @ApiModelProperty(notes = "Amount")
    private int amount;

    @ApiModelProperty(notes = "photoURL")
    private String photoURL;

    @ApiModelProperty(notes = "User name")
    private String userName;

    @ApiModelProperty(notes = "Email")
    private String email;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    @ApiModelProperty(notes = "Initial Value")
    private Double initialValue;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }
}
