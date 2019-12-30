package com.prabhjot.pschildmonitoringsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prabhjot.pschildmonitoringsystem.childeverything.ChildLoction;
import com.prabhjot.pschildmonitoringsystem.basicmethods.*;
public class childuser extends AppCompatActivity {

EditText e1,e2;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String mchildId,xxx,yyy;
    Boolean xxxx=false;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Childuid = "childUIDKey";
    public static final String Child = "childnameKey";
    public static final String Childage = "childageKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childuser);
        mAuth = FirebaseAuth.getInstance();
        e1=(EditText)findViewById(R.id.user3123);
        e2=(EditText)findViewById(R.id.age_of_the_kid);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(childuser.this,"No user is currently logged in! Kindly do that again",Toast.LENGTH_LONG).show();
        } else {

            mDatabase = FirebaseDatabase.getInstance().getReference("User/"+user.getUid()+"/child");
        }

    }

    public void finishsetup(View view){
        xxx=e1.getText().toString().trim();
        yyy=e2.getText().toString().trim();

        if(TextUtils.isEmpty(xxx)){
            e1.setError("Required!");
        }else if(TextUtils.isEmpty(yyy)){
            e2.setError("Required!");
        }else {


            new YourAsyncTask23(childuser.this).execute(xxx);
        }
    }
    private class YourAsyncTask23 extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;

        public YourAsyncTask23(childuser activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Initilizing..please wait");
            dialog.show();
        }
        @Override
        protected Void doInBackground(String... args) {
            // do background work her
            String usrnmed=args[0];
            back(usrnmed);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("MAIN","in background!");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(xxxx==false){

                mchildId = mDatabase.push().getKey();
                childhelper re = new childhelper(xxx,yyy,mchildId);
                mDatabase.child(mchildId).setValue(re);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Child, xxx);
                editor.putString(Childage,yyy);
                editor.putString(Childuid, mchildId);
                editor.commit();
                Toast.makeText(childuser.this,"User created!",Toast.LENGTH_LONG).show();
                Intent i=new Intent(childuser.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();


            }else{
                Toast.makeText(childuser.this,"User already Exist!",Toast.LENGTH_LONG).show();
                e1.setError("user already exists!");

            }

        }
    }
    public void back(final String usrcheck){
        Log.d("MAIN","in back!");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showdata(usrcheck,dataSnapshot);
                Log.d("MAIN","in datachange!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void showdata(String name,DataSnapshot dataSnapshot){
        Log.d("MAIN","in showdata!");
        childhelper user=new childhelper();
        xxxx=false;
        for(DataSnapshot ds : dataSnapshot.getChildren()){

            user.setUsername(ds.getValue(childhelper.class).getUsername());

            if(user.getUsername().equals(name)){
                e1.setError("User already exists!");
                xxxx=true;
                break;
            }
        }
    }

}
