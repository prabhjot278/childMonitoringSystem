package com.prabhjot.pschildmonitoringsystem.basicmethods;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;



public class messageservice extends Service {
    public static List<sendingmessage> ITEMS=new ArrayList<>();
   FirebaseAuth mAuth;
    DatabaseReference mref;
    public messageservice() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ITEMS.clear();
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String reference="Message/"+user.getUid();
        mref= FirebaseDatabase.getInstance().getReference(reference);

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    sendingmessage xx=dataSnapshot.getValue(sendingmessage.class);
                    ITEMS.add(xx);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                sendingmessage xx=dataSnapshot.getValue(sendingmessage.class);
                ITEMS.remove(xx);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Collections.reverse(ITEMS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
