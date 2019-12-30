package com.prabhjot.pschildmonitoringsystem.basicmethods;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prabhjot.pschildmonitoringsystem.childeverything.ChildLoction;
import com.prabhjot.pschildmonitoringsystem.childeverything.logshelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class logsservice extends Service {



    FirebaseAuth mAuth;
    String uid;
    String childuidd;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Childuid = "childUIDKey";
    public static final String Child = "childnameKey";
    public static final String Datekey = "dateKey";
    private static final int PERMISSIONS_REQUEST = 1;
    Timer t;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        mAuth= FirebaseAuth.getInstance();
        t= new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      loginToFirebase();
                                  }

                              },
//Set how long before to start calling the TimerTask (in milliseconds)
                0,
//Set the amount of time between each execution (in milliseconds)
                10000);

    }
   
    private void loginToFirebase() {
        FirebaseUser user=mAuth.getCurrentUser();
        uid=user.getUid();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        childuidd = sharedpreferences.getString(Childuid, "defaultStringIfNothingFound");
        if(user!=null){
            getCallDetails();
        }else{
            Toast.makeText(logsservice.this,"No user is currently logged in!",Toast.LENGTH_LONG).show();
        }
    }

    private void getCallDetails() {
        final String pathforcalllogs ="User/"+ uid + "/child/" + childuidd + "/data/call_logs";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(pathforcalllogs);

        //Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String pattern = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat formatter2=new SimpleDateFormat(pattern);
        Date shred=null;
        Date date1=null;
        // sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            StringBuffer sb = new StringBuffer();
            String phNumber = managedCursor.getString( number );
            String usrname=getContactRowIDLookupList(phNumber,logsservice.this);
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );


            String strDate = formatter.format(callDayTime);
            String strDate2=formatter2.format(callDayTime);
            long t=Integer.parseInt(callDuration);
            String pp=calculateTime(t);


            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            //sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
            //sb.append("\n----------------------------------");
            if(!sharedpreferences.contains(Datekey)){
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Datekey, strDate2);
                editor.commit();
                logshelper re = new logshelper(phNumber,callType,strDate,pp,usrname,dir);
                String mchildId = ref.push().getKey();
                ref.child(mchildId).setValue(re);
                Log.d("yupyup","inserted in shred");
            }else{
                String xxx = sharedpreferences.getString(Datekey, "defaultStringIfNothingFound");
                try{
                    shred=formatter2.parse(xxx);
                    date1=formatter2.parse(strDate2);
                }catch (Exception e){}

                if (shred.compareTo(date1)<0)
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Datekey, strDate2);
                    editor.commit();
                    Log.d("yupyup","inserted in if");
                    logshelper re = new logshelper(phNumber,callType,strDate,pp,usrname,dir);
                    String mchildId = ref.push().getKey();
                    ref.child(mchildId).setValue(re);
                }else{
                  //  Toast.makeText(logsservice.this,"already inserted!!",Toast.LENGTH_LONG).show();
                    Log.d("yupyup","not inserted");
                }
            }
           }
        managedCursor.close();
    }
    public static String calculateTime(long seconds) {

        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        String x=minute+"m "+second+"s";
        return x;

    }
    public static String getContactRowIDLookupList(String phoneNumber, Context context) {

        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="Not_Saved";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }
    
}
