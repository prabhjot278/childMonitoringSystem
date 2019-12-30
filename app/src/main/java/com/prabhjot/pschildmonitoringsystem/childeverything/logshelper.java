package com.prabhjot.pschildmonitoringsystem.childeverything;

import java.util.Date;

public class logshelper {
    public String phnumber;
    public String calltype;
    public String calldate;
    public String callduration;;
    public String calldir;
    public String callname;
    public logshelper() {
    }

    public logshelper(String phnumber, String calltype, String calldate, String callduration,String callname, String calldir) {
        this.phnumber = phnumber;
        this.calltype = calltype;
        this.calldate = calldate;
        this.callduration = callduration;
        this.calldir = calldir;
        this.callname = callname;
    }

    public String getCallname() {
        return callname;
    }

    public void setCallname(String callname) {
        this.callname = callname;
    }

    public String getCalldate() {
        return calldate;
    }

    public String getPhnumber() {
        return phnumber;
    }

    public void setPhnumber(String phnumber) {
        this.phnumber = phnumber;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }

    public String getCallduration() {
        return callduration;
    }

    public void setCallduration(String callduration) {
        this.callduration = callduration;
    }

    public String getCalldir() {
        return calldir;
    }

    public void setCalldir(String calldir) {
        this.calldir = calldir;
    }

    public void setCalldate(String calldate) {
        this.calldate = calldate;
    }





}
