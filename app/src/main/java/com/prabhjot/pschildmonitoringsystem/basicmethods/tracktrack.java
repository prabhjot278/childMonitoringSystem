package com.prabhjot.pschildmonitoringsystem.basicmethods;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prabhjot.pschildmonitoringsystem.ProgressHUD;
import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.loginActivity;
import  com.prabhjot.pschildmonitoringsystem.childeverything.*;

public class tracktrack extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 1;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1000;
    private static ProgressHUD a;
    TextView t1,t2;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracktrack);
        t1=(TextView)findViewById(R.id.lgning);
        t2=(TextView)findViewById(R.id.lgning2);
        b=(Button)findViewById(R.id.allowcs);

        t2.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);
      // Check GPS is enabled
        a = ProgressHUD.show(tracktrack.this, "Please wait", true, false, null);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            if ((a != null) && (a.isShowing()))
            {
                a.dismiss();
                a = null;
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, trackingservice.class));
        if ((a != null) && (a.isShowing()))
        {
            a.dismiss();
            a = null;
        }
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            Toast.makeText(tracktrack.this,"ohhhh yuck",Toast.LENGTH_LONG).show();
        }
        Intent i=new Intent(tracktrack.this,ChildLoction.class);
        startActivity(i);
        finish();
    }

    public void dismiss(){
        if ((a != null) && (a.isShowing()))
        {
            a.dismiss();
            a = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted

             startTrackerService();
        } else {
            dismiss();
            t2.setVisibility(View.VISIBLE);
            b.setVisibility(View.VISIBLE);
            PermissionUtils.setShouldShowStatus(this, Manifest.permission.ACCESS_FINE_LOCATION);

        }

        }

    public void allowaccess(View view){
        if(b.getText().toString()=="Next"){
            startTrackerService();
        }else{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                displayNeverAskAgainDialog();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST);
            }
        }
        }
    }
    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need Location access for performing necessary task. Please permit the permission through "
                + "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {
            startTrackerService();


        }else{
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
        }
    }

}
