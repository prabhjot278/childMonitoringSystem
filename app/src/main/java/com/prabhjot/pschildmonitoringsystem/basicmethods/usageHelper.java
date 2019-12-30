package com.prabhjot.pschildmonitoringsystem.basicmethods;

import android.support.v4.app.LoaderManager;

import java.util.Date;

public class usageHelper {
    String appname;
    String last_time_used;
    Long totaltime;

    public usageHelper() {

    }

    public Long getTotltime() {
        return totaltime;
    }

    public void setTotltime(Long totltime) {
        this.totaltime = totltime;
    }

    public usageHelper(String appname, String last_time_used, Long totltime) {
        this.appname = appname;
        this.last_time_used = last_time_used;
        this.totaltime=totltime;
    }
    public String getAppname() {
        return appname;
    }



    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getLast_time_used() {
        return last_time_used;
    }

    public void setLast_time_used(String last_time_used) {
        this.last_time_used = last_time_used;
    }
}
