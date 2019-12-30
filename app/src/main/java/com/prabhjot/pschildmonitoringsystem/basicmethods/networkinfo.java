package com.prabhjot.pschildmonitoringsystem.basicmethods;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class networkinfo {

    Context mContext;
    public networkinfo(Context mContext) {
        this.mContext = mContext;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
