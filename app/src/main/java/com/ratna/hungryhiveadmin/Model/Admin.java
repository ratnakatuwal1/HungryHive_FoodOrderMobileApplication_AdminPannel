package com.ratna.hungryhiveadmin.Model;

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
}
