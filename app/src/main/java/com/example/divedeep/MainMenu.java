package com.example.divedeep;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class MainMenu extends SurfaceView implements SurfaceHolder.Callback{
    public final static int ORIGINAL_SCREEN_HEIGHT = 1080;
    public final static int ORIGINAL_SCREEN_WIDTH = 2028;
    private MainActivity mainActivity;
    private MainMenu mainMenu;
    private GamePanel gamePanel;
    public static int WIDTH;
    public static int HEIGHT;
    public static float scaleFactorXMul = 1.0f;
    public static float scaleFactorYMul = 1.0f;
    public static Canvas canvas;
    private MenuThread menuThread;
    public static Resources res;
    private CustomImage background;
    private CustomImage boat;
    private Button playButton;
    private Paint waterPaint;
    private boolean isLeftHanded;
    private String playerName;
    public MainMenu(Context context, MainActivity mainActivity, int WIDTH, int HEIGHT, boolean isLeftHanded, String playerName) {
        super(context);

        this.mainActivity = mainActivity;
        this.isLeftHanded = isLeftHanded;
        this.playerName = playerName;

        // of phone's dimensions
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        mainMenu = this;

        scaleFactorXMul = 1.0f + ((WIDTH - ORIGINAL_SCREEN_WIDTH) * 1.0f / ORIGINAL_SCREEN_WIDTH);
        scaleFactorYMul = 1.0f + ((HEIGHT - ORIGINAL_SCREEN_HEIGHT) * 1.0f / ORIGINAL_SCREEN_HEIGHT);

        // create thread OBJECT
        menuThread = new MenuThread(getHolder(), this);

        getHolder().addCallback(this);

        setFocusable(true);

    }//end of  constructor

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        res = getResources();

        background = new CustomImage(BitmapFactory.decodeResource(res, R.drawable.menusheet), 5, 5);
        background.animation.setDelay(175);
        boat = new CustomImage(BitmapFactory.decodeResource(res, R.drawable.boatspritesheet), 15, 15, (int)(WIDTH * 0.6 / scaleFactorXMul), (int)(HEIGHT * 0.25 / scaleFactorYMul));
        boat.animation.setDelay(175);
        playButton = new Button(BitmapFactory.decodeResource(res, R.drawable.bouy), 10, 10, 1750, 525, true);
        playButton.animation.setDelay(175);

        waterPaint = new Paint();
        waterPaint.setColor(Color.argb(190, 0, 128, 255));

        //start the game loop thread
        menuThread.setRunning(true);
        menuThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        // adjust x and y the screen resolution by dividing by the factors
        int xPosition = (int) (event.getX() / scaleFactorXMul);
        int yPosition = (int) (event.getY() / scaleFactorYMul);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(playButton.isPressedRect(xPosition - 75, yPosition - 50))
                    startGame();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void update()
    {
        background.update();
        boat.update();
        playButton.update();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            // if our canvas exists then we can use it - not pointing to a null

            // save current state of our canvas
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX * scaleFactorXMul, scaleFactorY * scaleFactorYMul);
            canvas.drawColor(Color.rgb(90, 185, 255));

            boat.draw(canvas);
            playButton.draw(canvas);
            canvas.drawRect(0, 775, 2500, 2000, waterPaint);
            background.draw(canvas, -65, -60);

            // restore the saves canvas state back
            canvas.restoreToCount(savedState);
        }
    }

    private void startGame() {
        //create a black overlay view
        final View blackOverlay = new View(mainActivity);
        blackOverlay.setBackgroundColor(Color.BLACK);
        blackOverlay.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //add the overlay view to the main content view
        mainActivity.addContentView(blackOverlay, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //start fade animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(blackOverlay, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);

        //set up listener to remove the overlay and set the GamePanel
        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(gamePanel == null)
                    gamePanel = new GamePanel(mainActivity, mainActivity, mainMenu, WIDTH, HEIGHT, isLeftHanded, playerName);
                mainActivity.setContentView(gamePanel);
            }
        });

        fadeIn.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }
}
