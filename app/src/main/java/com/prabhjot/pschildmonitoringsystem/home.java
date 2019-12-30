package com.prabhjot.pschildmonitoringsystem;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prabhjot.pschildmonitoringsystem.childeverything.ChildLoction;

public class home extends Application{


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();

        if(user!=null){

            Intent i = new Intent(home.this, ChildLoction.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);


        }else{
            Toast.makeText(home.this,"no user",Toast.LENGTH_LONG).show();
        }
    }
}
