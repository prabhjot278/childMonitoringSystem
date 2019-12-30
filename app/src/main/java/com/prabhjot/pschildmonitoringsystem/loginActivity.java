package com.prabhjot.pschildmonitoringsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.prabhjot.pschildmonitoringsystem.childeverything.ChildLoction;
import com.prabhjot.pschildmonitoringsystem.parentevrything.Parent_Home;
import com.prabhjot.pschildmonitoringsystem.basicmethods.*;

public class loginActivity extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5;
    EditText usr,pass;
    Button b1;
    String mail,pas;
    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "mailKey";
    public static final String Password = "passKey";
    public static final String Child = "childnameKey";
    int x;
    private static ProgressHUD a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x=getIntent().getExtras().getInt("ID_");

        t1=(TextView)findViewById(R.id.lgn);
        t2=(TextView)findViewById(R.id.sup);
        t3=(TextView)findViewById(R.id.supper);
        t4=(TextView)findViewById(R.id.showhide);
        t5=(TextView)findViewById(R.id.supper23);
        usr=(EditText)findViewById(R.id.usrname);
        pass=(EditText)findViewById(R.id.password);
        b1=(Button)findViewById(R.id.loginbutton);


        mAuth = FirebaseAuth.getInstance();

        if(x==1){
            t1.setText("Parent Login");
            t2.setVisibility(View.VISIBLE);
        }else{
            t1.setText("Child Login");
            t3.setVisibility(View.VISIBLE);
            t2.setClickable(false);
        }

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(loginActivity.this,signupActivity.class);
                startActivity(i);
            }
        });

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(t4.getText().toString().equals("show")) {
                   t4.setText("hide");
                   pass.setTransformationMethod(null);
               }else{
                   t4.setText("show");
                   pass.setTransformationMethod(new PasswordTransformationMethod());
               }
            }
        });


    }
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent i=new Intent(loginActivity.this,welcome1.class);
        startActivity(i);
        finish();

        return;
    }
    public void yoyologin(View view){

        a = ProgressHUD.show(loginActivity.this, "loading", true, false, null);


      mail=usr.getText().toString().trim();
      pas=pass.getText().toString().trim();
      t5.setVisibility(View.GONE);
        mAuth.signInWithEmailAndPassword(mail, pas)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MAIN", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Name, mail);
                            editor.putString(Password, pas);
                            editor.commit();
                            if ((a != null) && (a.isShowing()))
                            {
                                a.dismiss();
                                a = null;
                            }
                            if(x==1){
                                Intent i = new Intent(loginActivity.this, Parent_Home.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }else {
                                if(sharedpreferences.contains(Child)){
                                    Intent i = new Intent(loginActivity.this,tracktrack .class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Intent i = new Intent(loginActivity.this, childuser.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }

                            }
                        } else {
                            if ((a != null) && (a.isShowing()))
                            {
                                a.dismiss();
                                a = null;
                            }
                            // If sign in fails, display a message to the user.
                            Log.w("MAIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            try
                            {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail)
                            {
                                Log.d("MAIN", "onComplete: invalid_email");
                                usr.setError("invalid email");

                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                            {
                                Log.d("MAIN", "onComplete: wrong_password");
                                //pass.setError("Wrong password");
                                t5.setVisibility(View.VISIBLE);

                            }
                            catch (Exception e)
                            {
                                Log.d("MAIN", "onComplete: " + e.getMessage());
                            }
                        }

                        // ...
                    }
                });



    }
}
