package com.prabhjot.pschildmonitoringsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prabhjot.pschildmonitoringsystem.childeverything.ChildLoction;
import com.prabhjot.pschildmonitoringsystem.parentevrything.Parent_Home;
import com.prabhjot.pschildmonitoringsystem.basicmethods.*;

public class MainActivity extends AppCompatActivity {

    private ProgressBar spinner;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "mailKey";
    public static final String Phone = "passKey";
    public static final String Child = "childnameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=firebaseAuth.getCurrentUser();
            /* New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    if(sharedpreferences.contains(Name)) {
                        if(user!=null){

                            if(sharedpreferences.contains(Child)) {
                                Intent i = new Intent(MainActivity.this, tracktrack.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                MainActivity.this.finish();
                                spinner.setVisibility(View.GONE);
                            }else{
                                Intent i = new Intent(MainActivity.this, Parent_Home.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                MainActivity.this.finish();
                                spinner.setVisibility(View.GONE);
                            }

                        }else{
                            Toast.makeText(MainActivity.this,"no user",Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(MainActivity.this, welcome1.class);
                            MainActivity.this.startActivity(mainIntent);
                            MainActivity.this.finish();
                            spinner.setVisibility(View.GONE);
                        }
                    }else{
                        /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(MainActivity.this, welcome1.class);
                        MainActivity.this.startActivity(mainIntent);
                        MainActivity.this.finish();
                        spinner.setVisibility(View.GONE);
                    }


                }
            }, SPLASH_DISPLAY_LENGTH);
    }

    }