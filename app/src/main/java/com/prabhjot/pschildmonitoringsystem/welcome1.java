package com.prabhjot.pschildmonitoringsystem;

import android.app.AppOpsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.prabhjot.pschildmonitoringsystem.basicmethods.*;

public class welcome1 extends AppCompatActivity {

    CardView c1,c2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);
        c1=(CardView)findViewById(R.id.parent_id);
        c2=(CardView)findViewById(R.id.child_id);

        c1.setCardElevation(10);
        c2.setCardElevation(10);

        //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));





        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkinfo c=new networkinfo(welcome1.this);
                final boolean networkstate=c.isNetworkAvailable();

                if(!checkUsageStatsAllowedornot()){
                    Intent usageIntent=new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    usageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(usageIntent);

                        Toast.makeText(welcome1.this,"Please Give Access!",Toast.LENGTH_LONG).show();


                }else{
                    if(networkstate){
                        Intent i=new Intent(welcome1.this,loginActivity.class);
                        i.putExtra("ID_",1);
                        startActivity(i);
                        finish();
                    }else{
                        Snackbar snackbar = Snackbar
                                .make(view, "No internet Connection!", Snackbar.LENGTH_LONG);

                        snackbar.show();

                    }

                }


            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkinfo c=new networkinfo(welcome1.this);
                final boolean networkstate=c.isNetworkAvailable();
                if(!checkUsageStatsAllowedornot()){
                    Intent usageIntent=new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    usageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(usageIntent);

                        Toast.makeText(welcome1.this,"Please Give Access!",Toast.LENGTH_LONG).show();


                }else{
                    if(networkstate){
                        Intent i=new Intent(welcome1.this,loginActivity.class);
                        i.putExtra("ID_",0);
                        startActivity(i);
                        finish();
                    }else{
                        Snackbar snackbar = Snackbar
                                .make(view, "No internet Connection!", Snackbar.LENGTH_LONG);

                        snackbar.show();

                    }

                }




            }
        });


    }



    public boolean checkUsageStatsAllowedornot(){
try{
    PackageManager packageManager=getPackageManager();
    ApplicationInfo applicationInfo=packageManager.getApplicationInfo(getPackageName(),0);
    AppOpsManager appOpsManager=(AppOpsManager)getSystemService(APP_OPS_SERVICE);
    int mode=appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,applicationInfo.uid,applicationInfo.packageName);
    return (mode==AppOpsManager.MODE_ALLOWED);
}catch (Exception e){
    Toast.makeText(welcome1.this,"Cannot find any Usage Stats",Toast.LENGTH_LONG).show();
    return false;
}

    }

}
