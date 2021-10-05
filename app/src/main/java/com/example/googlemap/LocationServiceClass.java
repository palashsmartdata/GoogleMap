package com.example.googlemap;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationServiceClass extends Service {
    FusedLocationProviderClient clientt;
    LocationRequest locationRequest;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public  void onCreate()
    {
        Toast.makeText(this, "in on create", Toast.LENGTH_SHORT).show();

        clientt = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10);
        locationRequest.setFastestInterval(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        super.onCreate();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(LocationServiceClass.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2048,intent1,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123")
                .setContentText("Running...")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setContentTitle("location Tracing..").setAutoCancel(false);

        if (Build.VERSION_CODES.O < Build.VERSION.SDK_INT)
        {
            NotificationChannel notificationChannel = new NotificationChannel("123", "Location", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Running........");
            notificationManager.createNotificationChannel(notificationChannel);
        }

            getLocation();
            Log.d("TAG", "ONSTART COMMAND: + ");
            startForeground(123, builder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        Toast.makeText(this, "getting location", Toast.LENGTH_SHORT).show();
        clientt.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location=locationResult.getLastLocation();

                Log.d("TAG", "onLocationResult: "+location.getLongitude());
                Toast.makeText(LocationServiceClass.this, "in Location", Toast.LENGTH_SHORT).show();
                //  LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                super.onLocationResult(locationResult);
            }
        }, Looper.myLooper());
    }

}
