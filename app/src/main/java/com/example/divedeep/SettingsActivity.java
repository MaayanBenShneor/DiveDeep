package com.example.divedeep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    RadioButton leftHanded;
    RadioButton rightHanded;
    boolean isLeftHanded;
    SeekBar volumeSeekBar;
    AudioManager audioManager;
    Button backButton;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemUI();

        setContentView(R.layout.activity_settings);
// get the someParameter from the calling activity
// using intent
        final Intent intent = getIntent();
        isLeftHanded = intent.getBooleanExtra("isLeftHanded", false);
        leftHanded = findViewById(R.id.leftHanded);
        leftHanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setThePassingParameter(true);

            }
        });

        rightHanded = findViewById(R.id.rightHanded);
        rightHanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setThePassingParameter(false);
            }
        });


        volumeSeekBar = findViewById(R.id.volumeBar);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(curVolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        backButton = findViewById(R.id.backBtnSettings);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThePassingParameter(isLeftHanded);
            }
        });

    }

    public void setThePassingParameter(boolean isLeft) {
        // create new intent
        Intent intent = new Intent();
        intent.putExtra("isLeftHanded", isLeft);

        // set This provides a pipeline for sending data back to the main Activity using setResult.
        // The setResult method takes an int result value and an Intent that is passed back to the calling Activity
        setResult(Activity.RESULT_OK, intent);

        // return to main activity
        finish();
    }
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
}
