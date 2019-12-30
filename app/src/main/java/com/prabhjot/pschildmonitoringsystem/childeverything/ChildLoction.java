package com.prabhjot.pschildmonitoringsystem.childeverything;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import java.text.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.database.ValueEventListener;
import com.prabhjot.pschildmonitoringsystem.MainActivity;
import com.prabhjot.pschildmonitoringsystem.basicmethods.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.basicmethods.trackingservice;
import com.prabhjot.pschildmonitoringsystem.parentevrything.Parent_Home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import com.prabhjot.pschildmonitoringsystem.basicmethods.*;

public class ChildLoction extends FragmentActivity implements OnMapReadyCallback,messagesFragment.OnListFragmentInteractionListener {
    private static final String TAG = ChildLoction.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private GoogleMap mMap;

    FirebaseAuth mAuth;
    String uid;
    String childuidd;
    String childnm;
    Boolean xxxxxx=false;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Childuid = "childUIDKey";
    public static final String Child = "childnameKey";
    public static final String Datekey = "dateKey";
    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_loction);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        childnm = sharedpreferences.getString(Child, "defaultStringIfNothingFound");

        getActionBar().setTitle(childnm);

        mAuth=FirebaseAuth.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST);
            }
        startService(new Intent(this, messageservice.class));
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.childloco_menu, menu);
        //getMenuInflater().inflate(R.menu.childloco_menu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
          if(xxxxxx){
              getSupportFragmentManager().popBackStack();
              xxxxxx=false;
          }else{
              AlertDialog.Builder builder = new AlertDialog.Builder(this);
              builder.setMessage("Are you sure you want to exit?")
                      .setCancelable(false)
                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int id) {
                              ChildLoction.this.finish();
                          }
                      })
                      .setNegativeButton("No", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int id) {
                              dialog.cancel();
                          }
                      });
              AlertDialog alert = builder.create();
              alert.show();

          }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signoutchild) {

            mAuth.signOut();
            stopService(new Intent(this, trackingservice.class));
            sharedpreferences.edit().clear();
            Intent i = new Intent(ChildLoction.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            ChildLoction.this.finish();


            return true;
        }else if(id==R.id.action_messageshow){
            xxxxxx=true;
            Fragment myObj = new messagesFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, myObj)
                    .addToBackStack(null)
                    .commit();
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(16);
        loginToFirebase();
    }
    private void loginToFirebase() {
        FirebaseUser user=mAuth.getCurrentUser();
        uid=user.getUid();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        childuidd = sharedpreferences.getString(Childuid, "defaultStringIfNothingFound");

        if(user!=null){
            subscribeToUpdates();
        }else{
            Toast.makeText(ChildLoction.this,"No user is currently logged in!",Toast.LENGTH_LONG).show();
        }
    }

    private void subscribeToUpdates() {
        final String path ="User/"+ uid + "/child/" + childuidd + "/data/location";

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference(path);
                if(dataSnapshot.hasChildren()) {
                    setMarker(dataSnapshot);
                }else{
                    Toast.makeText(ChildLoction.this,"Wait it's loading",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng location = new LatLng(lat, lng);
        if (!mMarkers.containsKey(key)) {
            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(childnm).position(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))));
        } else {
            mMarkers.get(key).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }
    private void getCallDetails() {
        final String pathforcalllogs ="User/"+ uid + "/child/" + childuidd + "/data/call_logs";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(pathforcalllogs);

        Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
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
                //logshelper re = new logshelper(phNumber,callType,strDate,pp,,dir);
                String mchildId = ref.push().getKey();
               // ref.child(mchildId).setValue(re);
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
                   // logshelper re = new logshelper(phNumber,callType,strDate,pp,dir);
                    String mchildId = ref.push().getKey();
                  //  ref.child(mchildId).setValue(re);
                }else{
                    Toast.makeText(ChildLoction.this,"already inserted!!",Toast.LENGTH_LONG).show();
                }
            }








        }
        managedCursor.close();
        //ccall.setText(sb);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            //getCallDetails();

            startService(new Intent(this, logsservice.class));
            startService(new Intent(this, browsinghistoryservice.class));

        } else {
           Toast.makeText(ChildLoction.this,"done",Toast.LENGTH_LONG).show();
}

    }

    public static String calculateTime(long seconds) {

        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        String x=minute+"m "+second+"s";
        return x;

    }

    @Override
    public void onListFragmentInteraction(sendingmessage item) {

    }
}