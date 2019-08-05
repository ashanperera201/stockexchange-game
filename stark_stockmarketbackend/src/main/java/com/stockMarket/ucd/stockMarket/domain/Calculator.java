package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class Calculator {
    //fields to be added
    @ApiModelProperty(notes = "Buy Date")
    private Date buyDate;
    @ApiModelProperty(notes = "Sell Date")
    private Date sellDate;
    @ApiModelProperty(notes = "Purchase Price")
    private double purchPrice;
    @ApiModelProperty(notes = "Quantity")
    private int quantity;
    @ApiModelProperty(notes = "Purchase Commission")
    private double purchComm;
    @ApiModelProperty(notes = "Sold Price")
    private double soldPrice;
    @ApiModelProperty(notes = "Sold Commission")
    private double soldComm;
    @ApiModelProperty(notes = "Total Investment")
    private double totalInvest;
    @ApiModelProperty(notes = "Gross Return")
    private double grossReturn;
    @ApiModelProperty(notes = "Profit")
    private double profit;
    @ApiModelProperty(notes = "Amount")
    private double amount;

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public double getPurchPrice() {
        return purchPrice;
    }

    public void setPurchPrice(double purchPrice) {
        this.purchPrice = purchPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchComm() {
        return purchComm;
    }

    public void setPurchComm(double purchComm) {
        this.purchComm = purchComm;
    }

    public double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public double getSoldComm() {
        return soldComm;
    }

    public void setSoldComm(double soldComm) {
        this.soldComm = soldComm;
    }

    public double getTotalInvest() {
        return totalInvest;
    }

    public void setTotalInvest(double totalInvest) {
        this.totalInvest = totalInvest;
    }

    public double getGrossReturn() {
        return grossReturn;
    }

    public void setGrossReturn(double grossReturn) {
        this.grossReturn = grossReturn;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
