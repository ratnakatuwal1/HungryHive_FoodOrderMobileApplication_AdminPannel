package com.ratna.hungryhiveadmin.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Admin {
    public String userId;
    public String email;
    public String password;
    public String name;
    public String address;
    public String phoneNo;
    public String profileImageUrl;

    public Admin(){

    }
    public Admin(String userId, String email, String password, String name, String address, String phoneNo, String profileImageUrl) {
        this.userId = userId;
        this.email = email;
        this.password = hashPassword(password);
        this.name = name;
        this.address = address;
        this.phoneNo = phoneNo;
        this.profileImageUrl = profileImageUrl;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());  // Convert password to byte array and hash it
            StringBuilder hexString = new StringBuilder();

            // Convert each byte to hex and append it to the string
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();  // Return the hashed password as a hex string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);  // Handle exception if the algorithm is not available
        }
    }
}
