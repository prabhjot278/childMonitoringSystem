package com.prabhjot.pschildmonitoringsystem;

public class childhelper {
    public String username;
    public String age;
    public String key;
    public childhelper() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public childhelper(String username, String age,String key) {
        this.username = username;
        this.age = age;
        this.key=key;
    }
    public String getUsername() {
        return username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
