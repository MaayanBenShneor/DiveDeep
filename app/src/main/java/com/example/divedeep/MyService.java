package com.example.divedeep;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {

    MediaPlayer mediaPlayer;
    private float musicVolume;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        // read the parameter from the calling activity
        mediaPlayer = MediaPlayer.create(this, R.raw.beach);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get the volume from calling activity
        if (intent != null) this.musicVolume = intent.getFloatExtra("musicVolume" , musicVolume);

        // set the volume on the music
        mediaPlayer.setVolume(musicVolume, musicVolume);

        mediaPlayer.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
    }
}

