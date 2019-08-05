package com.stockMarket.ucd.stockMarket.domain;

import io.swagger.annotations.ApiModelProperty;

public class HelpText {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "Header")
    private String header;

    @ApiModelProperty(notes = "Body")
    private String body;

    @ApiModelProperty(notes = "Formula")
    private String formula;

    @ApiModelProperty(notes = "Footer")
    private String footer;

    @ApiModelProperty(notes = "End")
    private String end;

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

}
