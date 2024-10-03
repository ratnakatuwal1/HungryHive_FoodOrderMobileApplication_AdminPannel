package com.ratna.hungryhiveadmin.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Admin {
    public String userId;
    public String email;
    public String name;
    public String address;
    public String phoneNo;
    public String profileImageUrl;

    public Admin(){

    }
    public Admin(String userId, String email, String name, String address, String phoneNo, String profileImageUrl) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNo = phoneNo;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
