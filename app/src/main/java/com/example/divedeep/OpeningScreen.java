package com.example.divedeep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class OpeningScreen extends AppCompatActivity
{
    private ImageButton startButton;
    private ImageButton quitButton;
    private boolean isLeftHanded;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        hideSystemUI();

        setContentView(R.layout.activity_opening_screen);

        startButton=findViewById(R.id.startbutton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                EditText input = new EditText(OpeningScreen.this);
                input.setHint("Enter Player Name");

                AlertDialog.Builder builder = new AlertDialog.Builder(OpeningScreen.this);
                builder.setTitle("Enter Your Name");
                builder.setView(input);

                builder.setPositiveButton("OK", (dialog, which) -> {
                   String playerName = input.getText().toString();
                   if(!playerName.trim().isEmpty()){
                       Intent intent = new Intent(OpeningScreen.this, MainActivity.class);
                       intent.putExtra("playerName", playerName);
                       intent.putExtra("isLeftHanded", isLeftHanded);
                       startActivity(intent);
                   }
                   else
                   {
                       input.setError("Name Cannot Be Empty");
                       return;
                   }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                builder.show();
            }
        });


        quitButton=findViewById(R.id.quitbutton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(OpeningScreen.this);
                builder.setMessage("Are You Sure You Want To Quit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)  {
                                finish();
                            }
                    }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opening_screen_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.settings_menu) {
            Intent settingsIntent = new Intent(OpeningScreen.this, SettingsActivity.class);
            settingsIntent.putExtra("isLeftHanded", isLeftHanded);
            settingActivityResultLauncher.launch(settingsIntent);
            return true;
        }
        else if (itemId == R.id.help) {
            Intent helpIntent = new Intent(OpeningScreen.this, HelpActivity.class);
            startActivity(helpIntent);
            return true;
        }
        else if (itemId == R.id.leaderboard) {
            Intent lbIntent = new Intent(OpeningScreen.this, ShowScoresActivity.class);
            startActivity(lbIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final ActivityResultLauncher<Intent> settingActivityResultLauncher = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null);
                {
                    isLeftHanded = result.getData().getBooleanExtra("isLeftHanded", false);
                }
            }
    );

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        ((View) decorView).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
