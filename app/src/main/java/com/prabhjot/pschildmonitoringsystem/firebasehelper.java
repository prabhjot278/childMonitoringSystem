package com.prabhjot.pschildmonitoringsystem;

public class firebasehelper {
    public String email;
    public String password;
    public String number;
    public String uid;

    public firebasehelper() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public firebasehelper(String email, String password,String number,String uid) {
        this.email = email;
        this.password = password;
        this.number=number;
        this.uid=uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
