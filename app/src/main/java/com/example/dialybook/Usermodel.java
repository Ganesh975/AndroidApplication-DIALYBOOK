package com.example.dialybook;

public class Usermodel {
    private String fullname,username,gender,address,mobile,email,password,presentbranch;

    public Usermodel() {
    }

    public Usermodel(String fullname, String username, String gender, String address, String mobile, String email, String password,String presentbranch) {
        this.fullname = fullname;
        this.username = username;
        this.gender = gender;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.presentbranch=presentbranch;
    }

    public String getPresentbranch() {
        return presentbranch;
    }

    public void setPresentbranch(String presentbranch) {
        this.presentbranch = presentbranch;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
