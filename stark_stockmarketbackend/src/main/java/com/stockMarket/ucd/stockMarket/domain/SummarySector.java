package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

public class SummarySector {

    @ApiModelProperty(notes = "Sector Id")
    private String sectorId;

    @ApiModelProperty(notes = "Sector Name")
    private String name;

    @ApiModelProperty(notes = "Total Quantity")
    private int totalQuantity;
    @ApiModelProperty(notes = "Total Amount")
    private Double totalAmount;

    public String getSectorId() {
        return sectorId;
    }

    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
