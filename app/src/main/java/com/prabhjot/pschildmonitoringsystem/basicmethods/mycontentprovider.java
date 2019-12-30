package com.prabhjot.pschildmonitoringsystem.basicmethods;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;


public class mycontentprovider extends ContentProvider{

    static
    {
        a("com.google.android.apps.chrome.browser-contract", "bookmarks");
        a("com.google.android.apps.chrome.browser-contract", "searches");
        a("com.google.android.apps.chrome.browser-contract", "history");
        a("com.google.android.apps.chrome.browser-contract", "combined");
    }
    private static Uri a(String paramString1, String paramString2)
    {
        StringBuilder localStringBuilder = new StringBuilder("content://");
        localStringBuilder.append(paramString1);
        localStringBuilder.append("/");
        localStringBuilder.append(paramString2);
        return Uri.parse(localStringBuilder.toString());
    }


    @Override
    public boolean onCreate() {
        return false;
    }


    @Override
    public Cursor query(Uri uri,String[] strings,String s,String[] strings1,String s1) {
        return null;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri,ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}

