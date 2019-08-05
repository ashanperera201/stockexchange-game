package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

public class ShareItem {

    @ApiModelProperty(notes = "share Id")
    private String shareId;

    @ApiModelProperty(notes = "company Id")
    private String companyId;

    @ApiModelProperty(notes = "user Id")
    private String userId;

    @ApiModelProperty(notes = "market Date")
    private Date marketDate;

    @ApiModelProperty(notes = "market Price")
    private int marketPrice;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getMarketDate() {
        return  marketDate;
    }

    public void setMarketDate(Date marketDate) {
        this.marketDate =  marketDate;
    }

    public int getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }
}
