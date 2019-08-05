package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Bank account.
 */

public class Bank {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(notes = "Date")
    private Date transDate;

    @ApiModelProperty(notes = "Bank Account amount")
    private Integer amount;

    @ApiModelProperty(notes = "User Id")
    private String userId;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public java.util.Date getDate() {
        return transDate;
    }

    public void setDate(java.util.Date date) {
        transDate = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "BankAmount{" +
                ", transDate='" + transDate + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

}
