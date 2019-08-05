package com.stockMarket.ucd.stockMarket.domain;


import io.swagger.annotations.ApiModelProperty;
import java.util.Date;


/**
 * A user.
 */
public class User {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "userId of the User")
    private String userId;

    @ApiModelProperty(notes = "username of the user")
    private String userName;

    @ApiModelProperty(notes = "email of the user")
    private String email;

    @ApiModelProperty(notes = "address of the user")
    private String address;

    @ApiModelProperty(notes = "nic of the user")
    private String nic;

    @ApiModelProperty(notes = "contactNo of the user")
    private int contactNo;

    @ApiModelProperty(notes = "dob of the user")
    private Date dob;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public int getContactNo() {
        return contactNo;
    }

    public void setContactNo(int contactNo) {
        this.contactNo = contactNo;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", nic='" + nic + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", dob=" + dob +
                '}';
    }
}
