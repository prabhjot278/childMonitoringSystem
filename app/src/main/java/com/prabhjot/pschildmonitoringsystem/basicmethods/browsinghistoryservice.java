package com.prabhjot.pschildmonitoringsystem.basicmethods;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.Browser;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;



import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prabhjot.pschildmonitoringsystem.R;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;


public class browsinghistoryservice extends Service{


    UsageStatsManager mUsageStatsManager;
    PackageManager pm;
    //UsageListAdapter mUsageListAdapter;
    final Uri BOOKMARKS_URI = Uri.parse("content://com.android.chrome.browser/bookmarks");
    final Uri Main_URI=Uri.parse("content://com.google.android.apps.chrome.browser-contract/bookmarks");
    final Uri uri =Uri.parse("chrome://History/");
    private static final String[] BOOKMARK_PROJECTION = {
            "title", // Browser.BookmarkColumns.TITLE
            "url", // Browser.BookmarkColumns.URL
    };
    // Copied from android.provider.Browser.BOOKMARKS_URI:
    private static final Uri BOOKMARKS_URI23 = Uri.parse("content://browser/bookmarks");

    private static final String BOOKMARK_SELECTION = "bookmark = 1 AND url IS NOT NULL";
    //Context ctx=this.getApplicationContext();


    final String[] HISTORY_PROJECTION = new String[]
            {
                    "_id", "url", "visits", "date", "bookmark", "title", "favicon", "created"
            };

    List<UsageStats> usageStatsList;
    List<String[]> titleURLs=new ArrayList();
    Spinner mSpinnerTimeSpan;
    private DateFormat mDateFormat = new SimpleDateFormat();
    Timer t;
    FirebaseAuth mAuth;
    String uid;
    String childuidd;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Childuid = "childUIDKey";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth= FirebaseAuth.getInstance();
        pm = getPackageManager();
        mUsageStatsManager = (UsageStatsManager) getApplication()
                .getSystemService(Context.USAGE_STATS_SERVICE);
        t= new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

                                 @Override
                                 public void run() {
                                     loginToFirebase();
                                  }}, 0,
                86400000);



    }
    private void loginToFirebase() {
        FirebaseUser user=mAuth.getCurrentUser();
        uid=user.getUid();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        childuidd = sharedpreferences.getString(Childuid, "defaultStringIfNothingFound");
        if(user!=null){
            getBrowserHist();
            //gethist();
            getsd();

        }else{
            Toast.makeText(browsinghistoryservice.this,"No user is currently logged in!",Toast.LENGTH_LONG).show();
        }
    }

    public void getBrowserHist() {
        final String pathforusage ="User/"+ uid + "/child/" + childuidd + "/data/app_usage/Daily";
        final String pathforusageweekly ="User/"+ uid + "/child/" + childuidd + "/data/app_usage/Weekly";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(pathforusage);
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(pathforusageweekly);



        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
        Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -7);
        cal2.set(Calendar.MILLISECOND, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.HOUR_OF_DAY, 0);

        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(),System.currentTimeMillis());
        final List<UsageStats> queryUsageStatsWeekly = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, cal2.getTimeInMillis(),System.currentTimeMillis());
        Collections.sort(queryUsageStats,new timeInForegroundComparator());
        Collections.sort(queryUsageStatsWeekly,new timeInForegroundComparator());

        //Log.d("yupyupi: ", ""+queryUsageStats);

        for (UsageStats app : queryUsageStats) {
            long lastTimeUsed =app.getLastTimeUsed();

            if(app.getTotalTimeInForeground()>20) {
                String xxx = app.getPackageName();
                String app_name="No name";
                try {
                    app_name  = (String) pm.getApplicationLabel(
                            pm.getApplicationInfo(xxx
                                    , PackageManager.GET_META_DATA));
                    Log.i("tuptup",app_name);
                }catch (Exception e){

                }
                usageHelper re = new usageHelper(app_name, mDateFormat.format(new Date(lastTimeUsed)),app.getTotalTimeInForeground());
                String mchildId = ref.push().getKey();
                ref.child(mchildId).setValue(re);
                //System.out.println(app.getPackageName() + " | " + (float) (app.getTotalTimeInForeground() / 1000));
                Log.d("yupyupi: ", xxx);
            }
        }
        for (UsageStats app : queryUsageStatsWeekly) {
            long lastTimeUsed =app.getLastTimeUsed();

            if(app.getTotalTimeInForeground()>20) {
                String xxx = app.getPackageName();
                String app_name="No name";
                try {
                    app_name  = (String) pm.getApplicationLabel(
                            pm.getApplicationInfo(xxx
                                    , PackageManager.GET_META_DATA));
                    Log.i("tuptup",app_name);
                }catch (Exception e){

                }
                usageHelper re = new usageHelper(app_name, mDateFormat.format(new Date(lastTimeUsed)),app.getTotalTimeInForeground());
                String mchildId = ref2.push().getKey();
                ref2.child(mchildId).setValue(re);
                //System.out.println(app.getPackageName() + " | " + (float) (app.getTotalTimeInForeground() / 1000));
                Log.d("yupyupi: ", xxx);
            }
        }
    }



    public void gethist(){
        //Cursor mCur = getContentResolver().query(BOOKMARKS_URI, HISTORY_PROJECTION, null, null, null);
        Log.d("yipyip","inside history top");
    Cursor mCur = this.getContentResolver()
            .query(uri, HISTORY_PROJECTION,
                    "bookmark = 0", null, null);
    //int numColumns = mCur.getCount();
    Log.d("yipyip", "inside history" + mCur);
    mCur.moveToFirst();

    String title = "";
    String url = "";
    String date = "";

    if (mCur.moveToFirst() && mCur.getCount() > 0) {
        while (mCur.isAfterLast() == false) {

            title = mCur.getString(mCur.getColumnIndex("title"));
            url = mCur.getString(mCur.getColumnIndex("url"));
            date = mCur.getString(mCur.getColumnIndex("date"));
            Log.d("yipyip", "inside history nd title is" + title);


            mCur.moveToNext();
        }
    }
    mCur.close();

    }

    private  class timeInForegroundComparator implements Comparator<UsageStats> {

        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getTotalTimeInForeground(), left.getTotalTimeInForeground());
        }
    }

    public void gethistory2(){

        String jsonString = "~/Library/Application Support/Google/Chrome/Default/Bookmarks";
        try {
            JSONObject json = new JSONObject(jsonString);
           // chrome.history.getVisits(json,);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void gethist2(){
        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;

        try
        {
            Class.forName ("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection ("jdbc:sqlite:/home/username/.config/chromium/Default/History");
            statement = connection.createStatement ();
            resultSet = statement
                    .executeQuery ("SELECT * FROM urls where visit_count > 100");

            while (resultSet.next ())
            {
                //System.out.println ("URL [" + resultSet.getString ("url") + "]" +
                  //      ", visit count [" + resultSet.getString ("visit_count") + "]");
                Log.d("yeyey","URL [" + resultSet.getString ("url") + "]"+
                        ", visit count [" + resultSet.getString ("visit_count") + "]");
            }
        }

        catch (Exception e)
        {
            e.printStackTrace ();
        }

        finally
        {
            try
            {
                resultSet.close ();
                statement.close ();
                connection.close ();
            }

            catch (Exception e)
            {
                e.printStackTrace ();
            }
        }

    }
    private boolean checkWriteExternalPermission()
    {
        String permission = "com.android.browser.permission.READ_HISTORY_BOOKMARKS";
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    public void getsd(){
        Log.d("chmchm", "ingethist");
            titleURLs.clear();
            Cursor cursor = getContentResolver().query(BOOKMARKS_URI23, BOOKMARK_PROJECTION,
                    BOOKMARK_SELECTION, null, null);
                if (cursor == null) {
                    Log.d("chmchm", "No cursor returned for bookmark query");

                    return;
                }else{
                    Log.d("chmchm", "inelse statement :"+cursor.moveToFirst());

                }
                while (cursor.moveToFirst()) {
                    Log.d("chmchm", cursor.getString(0));
                    titleURLs.add(new String[] { cursor.getString(0), cursor.getString(1) });
                }

        }





}
