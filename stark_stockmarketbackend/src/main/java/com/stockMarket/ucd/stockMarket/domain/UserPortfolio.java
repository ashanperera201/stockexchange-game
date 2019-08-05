package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

public class UserPortfolio {
    @ApiModelProperty(notes = "userId of the User")
    private String userId;

    @ApiModelProperty(notes = "username of the user")
    private String userName;

    @ApiModelProperty(notes = "Company Id")
    private String companyId;

    @ApiModelProperty(notes = "Company Name")
    private String companyName;

    @ApiModelProperty(notes = "Quantity")
    private int quantity;

    @ApiModelProperty(notes = "Market Price")
    private double marketPrice;

    @ApiModelProperty(notes = "profit or Loss")
    private double profitLoss;

    @ApiModelProperty(notes = "Total Amount")
    private double totalAmount;

    @ApiModelProperty(notes = "Type")
    private String type;

    @ApiModelProperty(notes = "Account Balance")
    private Double accBalance;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAccBalance() {
        return accBalance;
    }

    public void setAccBalance(Double accBalance) {
        this.accBalance = accBalance;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
