package com.prabhjot.pschildmonitoringsystem;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.prabhjot.pschildmonitoringsystem.signupActivity.*;

import java.util.concurrent.TimeUnit;

public class optActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText e1;
    Button b1;
    String xxx;
    String verification_Code,phnumber;
    CountDownTimer countDownTimer;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt);
        mAuth= FirebaseAuth.getInstance();
        e1=(EditText)findViewById(R.id.aheaduser2345);
        t1=(TextView)findViewById(R.id.timers);
        b1=(Button)findViewById(R.id.loginbutton234);
        verification_Code=getIntent().getExtras().getString("code");
        phnumber=getIntent().getExtras().getString("number");

        //Toast.makeText(optActivity.this,"")
        countDownTimer=new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                t1.setText("Time left: 00:" + millisUntilFinished / 1000+" sec");

            }

            public void onFinish() {
                Toast.makeText(optActivity.this,"Verification timeout!!",Toast.LENGTH_LONG).show();
                b1.setText("Back");
            }
        }.start();



    }

    public void verify(View view){
        if(b1.getText().toString().equals("Back")){
            signupActivity xx=new signupActivity();
            xx.resendotp();
            b1.setText("Verify");
        }else {
            countDownTimer.cancel();
            String input = e1.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_Code, input);
            signInWithPhoneAuthCredential(credential);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
         mAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user= task.getResult().getUser();
                            Toast.makeText(optActivity.this,"Verification succesful",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(optActivity.this,otherinfoActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(optActivity.this,"invalid OTP!",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
   // checking this when activity starts!!
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }


}
