package com.example.divedeep;

/**
 * @author Created by Eli Guriel on 22/03/2021.
 * @version version 2.00
 * @since version 2.00
 * Study Android,
 * Modi'in, Yachad high-school.
 *
 * This is the Main Activity of our game
 */


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver broadcastReceiver;
    private static int WIDTH;
    private static int HEIGHT;
    private float musicVolume = 0.5f;
    private boolean isLeftHanded;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent2 = getIntent();
        isLeftHanded = intent2.getBooleanExtra("isLeftHanded", isLeftHanded);
        playerName = intent2.getStringExtra("playerName");

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set screen landscape view
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // get device screen size for game panel use
        Display display = getWindowManager().getDefaultDisplay();
        // display size in pixels
        Point size = new Point();
        display.getSize(size);
        WIDTH = size.x;
        HEIGHT = size.y;

        hideSystemUI();

        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("musicVolume", musicVolume);

        startService(intent);

        setContentView(new MainMenu(this, this, WIDTH, HEIGHT, isLeftHanded, playerName));
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onStart() {
        super.onStart();

        broadcastReceiver = new BroadcastReceiver();
        IntentFilter filter1 = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(broadcastReceiver, filter1);

        IntentFilter filter2 = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(broadcastReceiver, filter2);

        IntentFilter filter3 = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(broadcastReceiver, filter3);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(new Intent(MainActivity.this, MyService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopService(new Intent(MainActivity.this, MyService.class));
    }

    /**
     * this methods used for full screen enable and disable
     */
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        ((View) decorView).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
