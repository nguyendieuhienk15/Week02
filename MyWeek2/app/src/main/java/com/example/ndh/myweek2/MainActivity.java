package com.example.ndh.myweek2;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMapClick(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.example.map","com.example.map.MapsActivity"));
        startActivity(intent);
    }

    public void onIntentClick(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.example.serviceapplication","com.example.serviceapplication.MainActivity"));
        startActivity(intent);
    }

    public void onFireClick(View view) {
        Intent intent= getPackageManager().getLaunchIntentForPackage("com.example.ndh.fcmapp");
        startActivity(intent);
    }

    public void onPoolClick(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.example.ndh.threadexecutor","com.example.ndh.threadexecutor.MainActivity"));
        startActivity(intent);
    }

    public void onListClick(View view) {
        Intent intent= getPackageManager().getLaunchIntentForPackage("com.example.android.xyztouristattractions");
        startActivity(intent);
    }
}
