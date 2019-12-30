package com.prabhjot.pschildmonitoringsystem.basicmethods;

public class sendingmessage {
    String time;
    String message;

    public sendingmessage() {

    }
    public sendingmessage(String time, String message) {
        this.time = time;
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
