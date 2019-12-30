package com.prabhjot.pschildmonitoringsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signupActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText e1,e2,e3;
    String num;
    String pricsdc;
    private DatabaseReference mDatabase23;
    String verification_code;
    LinearLayout l1;
    TextView t1,t2;
    Boolean xxxx = false;
    Button b1,b2;
    private static String TAG="you are in signup";
    CountDownTimer countDownTimer2,countDownTimer3;
    PhoneAuthProvider.ForceResendingToken mResendingtoken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks,mCallbacks2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        e1=(EditText)findViewById(R.id.aheaduser);
        e2=(EditText)findViewById(R.id.numuser);
        e3=(EditText)findViewById(R.id.aheaduser234);
        l1=(LinearLayout)findViewById(R.id.getget);
        b1=(Button)findViewById(R.id.loginbutton);
        b2=(Button)findViewById(R.id.loginbutton234);
        t1=(TextView)findViewById(R.id.justtext);
        t2=(TextView)findViewById(R.id.timers);
        e3.setVisibility(View.GONE);
        t2.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        mAuth= FirebaseAuth.getInstance();
        mDatabase23= FirebaseDatabase.getInstance().getReference("User");
        countDownTimer2=new CountDownTimer(63000, 1000) {

            public void onTick(long millisUntilFinished) {
                //t2.setText("Time left: 00:" + millisUntilFinished / 1000+" sec");
                t2.setText("RESEND OTP in: "+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                Toast.makeText(signupActivity.this,"Verification timeout!!",Toast.LENGTH_LONG).show();
                b2.setText("Resend OTP");

            }
        };



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                Toast.makeText(signupActivity.this,"Code Already sent!",Toast.LENGTH_LONG).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(signupActivity.this,"Invalid Mobile number!",Toast.LENGTH_LONG).show();
                    e2.setText("");

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(signupActivity.this,"Too many requests!!",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(signupActivity.this,"otp sent",Toast.LENGTH_LONG).show();

                verification_code=verificationId;
                mResendingtoken=token;
                Log.d(TAG, "onCodeSent:" + verificationId);
                e3.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.GONE);
                b1.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                countDownTimer2.start();

            }
        };
        mCallbacks2 = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                Toast.makeText(signupActivity.this,"Code Already sent!",Toast.LENGTH_LONG).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(signupActivity.this,"Invalid Mobile number!",Toast.LENGTH_LONG).show();
                    e2.setText("");

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(signupActivity.this,"Too many requests!!",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(signupActivity.this,"otp sent",Toast.LENGTH_LONG).show();

                verification_code=verificationId;
                mResendingtoken=token;

                b2.setText("Verify");


            }
        };

    }
    public void gotootp(View view){
        if (e1.getText().toString().length() == 0 && e2.getText().toString().length() < 10) {
            e1.setError("Required");
            e2.setError("Required");
        } else if (e1.getText().toString().length() == 0) {
            e1.setError("Required");
        } else if (e2.getText().toString().length() < 10) {
            e2.setError("Required");
        } else {

            num = "+" + e1.getText().toString() + e2.getText().toString();
            new YourAsyncTask2(signupActivity.this).execute(num);
        }


}
   public void verify(View view){
       if(b2.getText().toString().equals("Resend OTP")){
           resendotp();

       }else{
           countDownTimer2.cancel();
           String input = e3.getText().toString();
           PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code, input);
           signInWithPhoneAuthCredential(credential);
       }
    }



    public void sendotp(String no){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                no,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    public void resendotp(){
        countDownTimer2.start();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks2,
                mResendingtoken);        // OnVerificationStateChangedCallbacks

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user= task.getResult().getUser();
                            Toast.makeText(signupActivity.this,"Verification succesful",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(signupActivity.this,otherinfoActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(signupActivity.this,"invalid OTP!",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private class YourAsyncTask2 extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;

        public YourAsyncTask2(signupActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("please wait...");
            dialog.show();
        }
        @Override
        protected Void doInBackground(String... args) {
            // do background work her
            String phnumber=args[0];
            Log.d("MAIN","in background!"+phnumber);
            back(phnumber);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(!xxxx){
                Toast.makeText(signupActivity.this,"verification will start!",Toast.LENGTH_LONG).show();

                    sendotp(num);

            }else{
                Toast.makeText(signupActivity.this,"User already Exist!",Toast.LENGTH_LONG).show();

            }

        }
    }
    public void back(final String phcheck){
        Log.d("MAIN","in back!");
        mDatabase23.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showdata(phcheck,dataSnapshot);
                Log.d("MAIN","in datachange!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Log.d("MAIN","Cancelled :"+e);
            }
        });



    }

    public void showdata(String num,DataSnapshot dataSnapshot){
        Log.d("MAIN","in showdata!");
        firebasehelper user=new firebasehelper();
        xxxx=false;
        Log.i("MAIN","in showdata"+xxxx);
        for(DataSnapshot ds : dataSnapshot.getChildren()){

            user.setNumber(ds.getValue(firebasehelper.class).getNumber());
            //Toast.makeText(signupActivity.this,"yeey: "+user.getUsername()+ "and :"+name,Toast.LENGTH_LONG).show();

            if(user.getNumber().equals(num)){
                e2.setError("user with this number already exist!");
                xxxx=true;
                break;
            }
        }
    }


}
