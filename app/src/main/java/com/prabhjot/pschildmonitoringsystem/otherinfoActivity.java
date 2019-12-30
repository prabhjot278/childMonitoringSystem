package com.prabhjot.pschildmonitoringsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class otherinfoActivity extends AppCompatActivity {

    EditText e1,e2;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth,mAuth2;
    String phno,uid,xxx,yyy;
    Boolean get=false;
    Boolean xxxx;
    FirebaseUser prevUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherinfo);
        e1=(EditText)findViewById(R.id.userfrlgn);
        e2=(EditText)findViewById(R.id.passfrlgn);
        mAuth = FirebaseAuth.getInstance();
        mAuth2=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("User");

    }

    public void finishing(View view){
        xxx=e1.getText().toString().trim();
        yyy=e2.getText().toString().trim();
        if(TextUtils.isEmpty(xxx)){
            e1.setError("Required!");
        }else if(TextUtils.isEmpty(yyy)){
            e2.setError("Required!");
        }else if(yyy.length()<6){
            e2.setError("Password should be more than 6 character");
        }else{
            if(isValidEmailId(xxx)){
                prevUser= mAuth.getCurrentUser();
                phno=prevUser.getPhoneNumber();
                uid=prevUser.getUid();
                if (prevUser == null) {
                    Toast.makeText(otherinfoActivity.this,"No user is currently logged in!",Toast.LENGTH_LONG).show();
                } else {
                    new YourAsyncTask(otherinfoActivity.this).execute(xxx,yyy,phno,uid);

                }
            }else{
                Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                e1.setError("InValid");

            }

        }

    }
    private class YourAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;

        public YourAsyncTask(otherinfoActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Signing Up, please wait.");
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
                firebasehelper re=new firebasehelper(xxx,yyy,phno,uid);
                mDatabase.child(uid).setValue(re);
                Toast.makeText(otherinfoActivity.this,"User created!",Toast.LENGTH_LONG).show();
                linking(xxx,yyy);
            }else{
                Toast.makeText(otherinfoActivity.this,"User already Exist!",Toast.LENGTH_LONG).show();

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
        firebasehelper user=new firebasehelper();
        xxxx=false;
        for(DataSnapshot ds : dataSnapshot.getChildren()){

           user.setEmail(ds.getValue(firebasehelper.class).getEmail());

           if(user.getEmail().equals(name)){
               e1.setError("User already exists!");
            xxxx=true;
            break;
          }
        }
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    // for authenticating individually with email and password!
    public void authenticateemail(final String email, final String pass){

        mAuth2.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MAIN", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            linking(email,pass);
                            //Log.d("MAIN", "yupyup"+user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MAIN", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(otherinfoActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    public void linking(String email,String pass){
        AuthCredential credential = EmailAuthProvider.getCredential(email, pass);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MAIN", "linkWithCredential:success");
                            //FirebaseUser user = task.getResult().getUser();
                            Intent i=new Intent(otherinfoActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Log.w("MAIN", "linkWithCredential:failure", task.getException());
                            Toast.makeText(otherinfoActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }



}
