package com.example.divedeep;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShowScoresActivity extends AppCompatActivity {

    Button backBtn;
    public static final String SHARED_PREFS_NAME = "HighScores";
    public static final String SCORES_KEY = "scores";
    private SharedPreferences sp;
    private int[] medalImages = {
            R.drawable.goldmedal,
            R.drawable.silvermedal,
            R.drawable.bronzemedal,
            R.drawable.transparent
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores);

        hideSystemUI();

        backBtn = findViewById(R.id.backBtnScores);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sp = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);

        // Retrieve the scores from SharedPreferences
        List<StoredScore> scoresList = retrieveScores();

        // Sort the scores, and update the ListView
        BubbleSort sorter = new BubbleSort(new ArrayList<>(scoresList));
        sorter.bubbleSort();
        scoresList = sorter.getSortedListViewItems();

        // Set up the ListView with CustomAdapter
        CustomAdapter adapter = new CustomAdapter(this, new ArrayList<>(scoresList), medalImages);
        ListView listView = findViewById(R.id.scoresListView);
        listView.setAdapter(adapter);
    }

    private List<StoredScore> retrieveScores() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String scoresJson = sp.getString(SCORES_KEY, "[]");  // Corrected key

        Moshi moshi = new Moshi.Builder().build();
        Type listOfScoresType = Types.newParameterizedType(List.class, StoredScore.class);
        JsonAdapter<List<StoredScore>> adapter = moshi.adapter(listOfScoresType);

        try {
            List<StoredScore> scores = adapter.fromJson(scoresJson);
            if (scores != null) {
                return scores;
            } else {
                Log.e("RetrieveScores", "No scores were found in SharedPreferences.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RetrieveScores", "Failed to parse scores JSON.", e);
        }
        return new ArrayList<>();
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
