package com.prabhjot.pschildmonitoringsystem.parentevrything;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prabhjot.pschildmonitoringsystem.BuildConfig;
import com.prabhjot.pschildmonitoringsystem.MainActivity;
import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.basicmethods.messageservice;
import com.prabhjot.pschildmonitoringsystem.basicmethods.sendingmessage;
import com.prabhjot.pschildmonitoringsystem.basicmethods.usageHelper;
import com.prabhjot.pschildmonitoringsystem.childeverything.logshelper;
import com.prabhjot.pschildmonitoringsystem.childeverything.messagesFragment;
import com.prabhjot.pschildmonitoringsystem.childhelper;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.prabhjot.pschildmonitoringsystem.childeverything.*;

public class Parent_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        childFragment.OnListFragmentInteractionListener,
        logsFragment.OnListFragmentInteractionListener,
        usageFragment.OnListFragmentInteractionListener,
        loadingFragment.OnFragmentInteractionListener,
        sendmessageFragment.OnFragmentInteractionListener,
        messagesFragment.OnListFragmentInteractionListener
        {

    private FirebaseAuth mAuth;
    private DatabaseReference duffer;
    private static FragmentManager fragmentManager;
    TextView t1,t2;
    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST = 100;
    public static final Map<String, String> ITEM_MAP = new HashMap<String, String>();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "mailKey";
    public static final String Password = "passKey";
    String uid;
    int counter=0;
    private static final String TAG = Parent_Home.class.getSimpleName();

    public float colors22[]={BitmapDescriptorFactory.HUE_ROSE,BitmapDescriptorFactory.HUE_AZURE,BitmapDescriptorFactory.HUE_BLUE
                           ,BitmapDescriptorFactory.HUE_CYAN,BitmapDescriptorFactory.HUE_GREEN,BitmapDescriptorFactory.HUE_MAGENTA,
       BitmapDescriptorFactory.HUE_ORANGE,BitmapDescriptorFactory.HUE_RED,BitmapDescriptorFactory.HUE_VIOLET,BitmapDescriptorFactory.HUE_YELLOW};
    public long childcount;
    public List<String> mlistchild=new ArrayList();
    public List<String> mlistchildname=new ArrayList();
    public List<String> mlistchildage=new ArrayList();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    public static List<childhelper> xxxxxxx=new ArrayList<>();
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xxxxxxx.clear();
        setContentView(R.layout.activity_parent__home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
    fragmentManager=getSupportFragmentManager();
        FirebaseUser user = mAuth.getCurrentUser();
        duffer=FirebaseDatabase.getInstance().getReference("User/"+user.getUid()+"/child");
        startService(new Intent(this, messageservice.class));
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       // mapFragment= (SupportMapFragment) getSupportFragmentManager()
         //       .findFragmentById(R.id.map2);
        //mapFragment.getMapAsync(this);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }

        // R.id.map is a FrameLayout, not a Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.map2, mapFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //we need to declare view to get that view from navigation drawer and use it for our use.
        View header=navigationView.getHeaderView(0);
        t1=(TextView)header.findViewById(R.id.nameofuser);
        t2=(TextView)header.findViewById(R.id.textViewemail);

        t1.setText("PARENT");
        t2.setText(user.getEmail());



        Log.d("IN Parent", "inside parent home");



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Parent_Home.this.finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parent__home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {

            mAuth.signOut();
            //stopService(new Intent(this, trackingservice.class));
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(Name);
            editor.remove(Password);
            editor.commit();
            Intent i = new Intent(Parent_Home.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            Parent_Home.this.finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Location) {
                  xxxxxxx.clear();
               mapFragment = SupportMapFragment.newInstance();
                mapFragment.getMapAsync(this);


        //    R.id.map is a FrameLayout, not a Fragmentcjn
            cjndcm();
            getSupportFragmentManager().beginTransaction().replace(R.id.map2, mapFragment).commit();


        } else if (id == R.id.nav_calls) {
            mMarkers.clear();
            Bundle bundle = new Bundle();
            bundle.putString("params", "calls");
// set MyFragment Arguments
            Fragment myObj = new childFragment();
            myObj.setArguments(bundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.map2, myObj)
                    .commit();

            //ListView l=(ListView)findViewById(R.id.chimpuchild);


        } else if (id == R.id.nav_usage) {
            mMarkers.clear();
            Bundle bundle = new Bundle();
            bundle.putString("params", "usage");
// set MyFragment Arguments
            Fragment myObj = new childFragment();
            myObj.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map2, myObj)
                    .commit();

        } else if (id == R.id.nav_manage) {
            mMarkers.clear();
            Fragment myObj = new messagesFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map2, myObj)
                    .commit();

        } else if (id == R.id.nav_share) {
            try {
                mMarkers.clear();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Child Monitoring");
                String shareMessage= "Download this application \n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        } else if (id == R.id.nav_send) {
            mMarkers.clear();
            Fragment myObj = new sendmessageFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.map2, myObj)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(16);
        Log.d("IN Parent","map is ready ");
        duffer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    childcount = dataSnapshot.getChildrenCount();
                    Log.d("IN Parent", "child count: " + childcount);
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        counter++;
                        String xx=snap.getValue(childhelper.class).getUsername();
                        String xxx=snap.getValue(childhelper.class).getAge();
                        mlistchild.add(snap.getKey());
                        mlistchildname.add(xx);
                        mlistchildage.add(xxx);
                        xxxxxxx.add(snap.getValue(childhelper.class));
                    }
                    loginToFirebase();
                }else{
                    Toast.makeText(Parent_Home.this,"No child found for this Parent!!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("IN Parent"," "+databaseError);
            }
        });


    }
    public void cjndcm(){
        mMap.setMaxZoomPreference(16);
        duffer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    childcount = dataSnapshot.getChildrenCount();
                    Log.d("IN Parent", "child count: " + childcount);
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        counter++;
                        String xx=snap.getValue(childhelper.class).getUsername();
                        String xxx=snap.getValue(childhelper.class).getAge();
                        mlistchild.add(snap.getKey());
                        mlistchildname.add(xx);
                        mlistchildage.add(xxx);
                        xxxxxxx.add(snap.getValue(childhelper.class));
                    }
                    loginToFirebase();
                }else{
                    Toast.makeText(Parent_Home.this,"No child found for this Parent!!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("IN Parent"," "+databaseError);
            }
        });

    }
    private void loginToFirebase() {
        FirebaseUser user=mAuth.getCurrentUser();
        uid=user.getUid();

        for(int i=0;i<childcount;i++){
            if(user!=null){
                Log.d("IN Parent"," here in for loop");
                subscribeToUpdates(mlistchild.get(i),mlistchildname.get(i));
            }else{
                Toast.makeText(Parent_Home.this,"No user is currently logged in!",Toast.LENGTH_LONG).show();
            }

        }


    }
    private void subscribeToUpdates(final String childuidd, final String childname) {
        final String path ="User/"+ uid + "/child/" + childuidd + "/data/location";
        Log.d("IN Parent", "key: "+childuidd);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference(path);
                if(dataSnapshot.hasChildren()) {
                    setMarker(dataSnapshot,childuidd,childname);
                }else{
                    Toast.makeText(Parent_Home.this,"Wait it's loading",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    private void setMarker(DataSnapshot dataSnapshot,String retet,String childname) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        String key = retet;
        final int random = new Random().nextInt(10) ;
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng location = new LatLng(lat, lng);
        Log.d("IN Parent", "inside of set marker");


        if (!mMarkers.containsKey(key)) {
            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(childname).position(location).icon(BitmapDescriptorFactory.defaultMarker(colors22[random]))));
            Log.d("IN Parent", "inside of set marker"+key);
        } else {
            mMarkers.get(key).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }


    @Override
    public void onListFragmentInteraction(childhelper item) {

    }

    @Override
    public void onListFragmentInteraction(usageHelper item) {

    }

    @Override
    public void onListFragmentInteraction(logshelper item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

            @Override
            public void onListFragmentInteraction(sendingmessage item) {

            }
        }


