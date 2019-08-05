package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

public class Share {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(notes = "share Id")
    private String shareId;
    @ApiModelProperty(notes = "company Id")
    private String companyId;

    @ApiModelProperty(notes = "original Price")
    private int originalPrice;

    @ApiModelProperty(notes = "current Price")
    private double currentPrice;

    @ApiModelProperty(notes = "quantity")
    private int quantity;

    @ApiModelProperty(notes = "Company Name")
    private String name;

    @ApiModelProperty(notes ="Gain Loss")
    private Double gainLoss;

    public Double getGainLoss() {
        return gainLoss;
    }

    public void setGainLoss(Double gainLoss) {
        this.gainLoss = gainLoss;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
