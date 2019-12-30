package com.prabhjot.pschildmonitoringsystem.basicmethods;


import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prabhjot.pschildmonitoringsystem.R;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.Manifest;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class trackingservice extends Service {

        private static final String TAG = trackingservice.class.getSimpleName();
        FirebaseAuth mAuth;
        String uid;
        String childuidd;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Childuid = "childUIDKey";


        @Override
        public IBinder onBind(Intent intent) {return null;}

        @Override
        public void onCreate() {
            super.onCreate();
            initChannels(this);
            buildNotification();

            mAuth=FirebaseAuth.getInstance();
            loginToFirebase();
        }

        private void buildNotification() {
            String stop = "stop";
            registerReceiver(stopReceiver, new IntentFilter(stop));
            PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                    this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
            // Create the persistent notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"default")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.notification_text))
                    .setOngoing(true)
                    .setContentIntent(broadcastIntent)
                    .setSmallIcon(R.drawable.parent);
            startForeground(1, builder.build());
        }

        protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "received stop broadcast");
                // Stop the service when the notification is tapped
                unregisterReceiver(stopReceiver);
                stopSelf();
            }
        };

        private void loginToFirebase() {
            FirebaseUser user=mAuth.getCurrentUser();
            uid=user.getUid();
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            childuidd = sharedpreferences.getString(Childuid, "defaultStringIfNothingFound");
            if(user!=null){
                requestLocationUpdates();
            }else{
                Toast.makeText(trackingservice.this,"No user is currently logged in!",Toast.LENGTH_LONG).show();
            }
        }

        private void requestLocationUpdates() {
            Log.d(TAG,"yeeey in loction");
            LocationRequest request = new LocationRequest();
            request.setInterval(10000);
            request.setFastestInterval(5000);
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
            final String path = "User/"+ uid + "/child/" + childuidd + "/data";
            Log.d(TAG, "yeeeeeey "+ path);

            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                // Request location updates and when an update is
                // received, store the location in Firebase
                client.requestLocationUpdates(request, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            Log.d(TAG, "location update " + location);
                            ref.child("location").setValue(location);
                        }else{
                            Log.d("yeeeeeeeey", "location update " + location);
                            Toast.makeText(trackingservice.this,"no location",Toast.LENGTH_LONG).show();
                        }
                    }
                }, null);
            }
        }
    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }

    }


