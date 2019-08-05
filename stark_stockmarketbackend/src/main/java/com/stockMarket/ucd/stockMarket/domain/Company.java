package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

public class Company {

    @ApiModelProperty(notes = "Company Id")
    private String companyId;
    @ApiModelProperty(notes = "Company Name")
    private String name;
    @ApiModelProperty(notes = "Address")
    private String address;
    @ApiModelProperty(notes = "Sector Id")
    private String sectorId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSectorId() {
        return this.sectorId;
    }

    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }
}
