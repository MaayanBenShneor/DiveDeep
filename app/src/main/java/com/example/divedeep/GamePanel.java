package com.example.divedeep;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    public final static int ORIGINAL_SCREEN_HEIGHT = 1080;
    public final static int ORIGINAL_SCREEN_WIDTH = 2028;
    public static int WIDTH;
    public static int HEIGHT;

    // canvas of the game, we draw our pictures on this CANVAS
    public static Canvas canvas;
    private MainActivity mainActivity;
    private MainMenu mainMenu;
    private GestureDetectorCompat gestureDetector;
    private GameThread thread;
    public static float scaleFactorXMul = 1.0f;
    public static float scaleFactorYMul = 1.0f;
    public static Resources res;
    private Paint depthPaint;
    public MapGenerator mapGen;
    private CustomImage boat;
    private Player player;
    private String playerName;
    private Joystick joystick;
    private int joystickPointerId = 0;
    public Button mineButton;
    private Button shopButton;
    private Paint goldPaint;
    private CustomImage goldUI;
    private CustomImage shop;
    private int speedBoostPrice = 5, oxygenBoostPrice = 10, goldBoostPrice = 30;
    private Button speedBoostBtn, oxygenBoostBtn, goldBoostBtn;
    private Paint pricePaint;
    private GamePanel gamePanel = this;
    private Paint vignettePaint;
    private Rect vignetteRect;
    int vignetteRadius = 5000;
    private Paint deathPaint;
    private boolean gameEnded;
    private Paint scorePaint;
    private boolean playerIsDead = false;
    private MediaPlayer breathingAmbient, purchaseSfx, goldGrab, oxygenFill;
    private boolean filledOxygen;
    private boolean isLeftHanded;

    //lets create the constructor of our new class,that is going to help us calling objects and methods!
    public GamePanel(Context context, MainActivity mainActivity, MainMenu mainMenu, int WIDTH, int HEIGHT, boolean isLeftHanded, String playerName) {

        super(context);

        // of phone's dimensions
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        this.isLeftHanded = isLeftHanded;
        this.playerName = playerName;

        scaleFactorXMul = 1.0f + ((WIDTH - ORIGINAL_SCREEN_WIDTH) * 1.0f / ORIGINAL_SCREEN_WIDTH);
        scaleFactorYMul = 1.0f + ((HEIGHT - ORIGINAL_SCREEN_HEIGHT) * 1.0f / ORIGINAL_SCREEN_HEIGHT);

        this.mainActivity = mainActivity;
        this.mainMenu = mainMenu;

        //sound effects
        breathingAmbient = MediaPlayer.create(context, R.raw.breathing);
        breathingAmbient.setLooping(true);
        breathingAmbient.start();

        purchaseSfx = MediaPlayer.create(context, R.raw.purchase);
        goldGrab = MediaPlayer.create(context, R.raw.coindrop);
        oxygenFill = MediaPlayer.create(context, R.raw.air);

        // gesture-s
        this.gestureDetector = new GestureDetectorCompat(this.getContext(), this);
        gestureDetector.setOnDoubleTapListener(this);

        // create thread OBJECT
        thread = new GameThread(getHolder(), this);

        getHolder().addCallback(this);

        setFocusable(true);

    }//end of  constructor
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        res = getResources();

        mapGen = new MapGenerator(400, 400);
        boat = new CustomImage(BitmapFactory.decodeResource(res, R.drawable.boatspritesheet), 15, 15, mapGen);
        boat.animation.setDelay(250);

        if(!isLeftHanded)
        {
            joystick = new Joystick((int)(WIDTH * 0.15f / scaleFactorXMul), (int)(HEIGHT * 0.725f / scaleFactorYMul), 125, 62);
            mineButton = new Button(BitmapFactory.decodeResource(res, R.drawable.pickaxe), 1, 1, 1600, 600, false);
            shopButton = new Button(BitmapFactory.decodeResource(res, R.drawable.shopbutton), 1, 1, 1600, HEIGHT * 0.7f / scaleFactorYMul, true);
        }
        else
        {
            joystick = new Joystick((int)(WIDTH * 0.85f / scaleFactorXMul), (int)(HEIGHT * 0.725f / scaleFactorYMul), 125, 62);
            mineButton = new Button(BitmapFactory.decodeResource(res, R.drawable.pickaxe), 1, 1, (int)(WIDTH * 0.15f / scaleFactorXMul), 600, false);
            shopButton = new Button(BitmapFactory.decodeResource(res, R.drawable.shopbutton), 1, 1, (int)(WIDTH * 0.15f / scaleFactorXMul), HEIGHT * 0.7f / scaleFactorYMul, true);
        }

        player = new Player(gamePanel, joystick, BitmapFactory.decodeResource(res, R.drawable.diverswim), 8, 8);
        shop = new CustomImage(BitmapFactory.decodeResource(res, R.drawable.shop), 1, 1);
        shop.setShow(false);

        speedBoostBtn = new Button(BitmapFactory.decodeResource(res, R.drawable.boosterbtn), 1, 1, 450, 500, true);
        oxygenBoostBtn = new Button(BitmapFactory.decodeResource(res, R.drawable.boosterbtn), 1, 1, 865, 500, true);
        goldBoostBtn = new Button(BitmapFactory.decodeResource(res, R.drawable.boosterbtn), 1, 1, 1280, 500, true);

        depthPaint = new Paint();
        depthPaint.setColor(Color.RED);
        depthPaint.setTextSize(50);
        depthPaint.setTypeface(Typeface.DEFAULT_BOLD);

        deathPaint = new Paint();
        deathPaint.setColor(Color.WHITE);
        deathPaint.setTextSize(150);
        deathPaint.setTypeface(Typeface.DEFAULT_BOLD);

        goldPaint = new Paint();
        goldPaint.setColor(Color.rgb(255, 205, 0));
        goldPaint.setTextSize(50);
        goldPaint.setTypeface(Typeface.DEFAULT_BOLD);

        pricePaint = new Paint();
        pricePaint.setColor(Color.rgb(255, 205, 0));
        pricePaint.setTextSize(38);
        pricePaint.setTypeface(Typeface.DEFAULT_BOLD);

        scorePaint = new Paint();
        scorePaint.setColor(Color.rgb(255, 205, 0));
        scorePaint.setTextSize(75);
        scorePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));

        goldUI = new CustomImage(BitmapFactory.decodeResource(res, R.drawable.goldbar), 1, 1, WIDTH + 300, 150);
        goldUI.flipImage(true, false);

        //start the game loop thread
        thread.setRunning(true);
        thread.start();

        //fade out
        final View blackOverlay = new View(mainActivity);
        blackOverlay.setBackgroundColor(Color.BLACK);
        blackOverlay.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mainActivity.addContentView(blackOverlay, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //start fade animation
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(blackOverlay, "alpha", 1f, 0f);
        fadeOut.setDuration(1000);

        fadeOut.start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

                mainActivity.setContentView(mainMenu);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//end while

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // gestures events
        this.gestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        int xPosition, yPosition;

        // adjust x and y the screen resolution by dividing by the factors
        xPosition = (int) (event.getX() / scaleFactorXMul);
        yPosition = (int) (event.getY() / scaleFactorYMul);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(shopButton.isPressedRect(xPosition, yPosition) && shopButton.isShow()) {
                    if(shop.isShow()) //close the shop
                    {
                        shopButton.setNewBitmap(BitmapFactory.decodeResource(res, R.drawable.shopbutton), 1, 1);
                        shop.setShow(false);
                    }
                    else //open the shop
                    {
                        shopButton.setNewBitmap(BitmapFactory.decodeResource(res, R.drawable.backbutton), 1, 1);
                        shop.setShow(true);
                    }
                }

                if(shop.isShow())
                {
                    if(goldBoostBtn.isPressedRect(xPosition, yPosition) && player.getGoldAmount() >= goldBoostPrice && player.goldMultiplier == 1)
                    {
                        purchaseSfx.start();
                        player.spendGold(goldBoostPrice);
                        player.goldMultiplier = 2;
                    }
                    else if(speedBoostBtn.isPressedRect(xPosition, yPosition) && player.getGoldAmount() >= speedBoostPrice)
                    {
                        purchaseSfx.start();
                        player.spendGold(speedBoostPrice);
                        player.mulSpeed(1.2f);
                        speedBoostPrice += 5;
                    }
                    else if(oxygenBoostBtn.isPressedRect(xPosition, yPosition) && player.getGoldAmount() >= oxygenBoostPrice)
                    {
                        purchaseSfx.start();
                        player.spendGold(oxygenBoostPrice);
                        player.oxygenBar.changeTimeToEmpty((int)(player.oxygenBar.timeToEmpty * 1.25f));
                        oxygenBoostPrice += 5;
                    }
                }

                if(joystick.getIsPressed())
                {
                    //touched outside of joystick while joystick is pressed
                    if(mineButton.isPressedRadius(event.getX(1) / scaleFactorXMul - 100, event.getY(1) / scaleFactorYMul))
                    {
                        player.mineGold();
                        goldGrab.start();
                    }
                    break;
                }
                else if(joystick.isPressed(xPosition, yPosition)) {
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }
                else
                    //touched outside of joystick
                    if(mineButton.isPressedRadius(xPosition - 100, yPosition)) {
                        player.mineGold();
                        goldGrab.start();
                    }
                break;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed() && !shop.isShow() && !playerIsDead) {
                    joystick.setDirection(xPosition, yPosition);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                break;
        }
        return true;
    }

    @SuppressLint("SuspiciousIndentation")
    public void update() {
        boat.update();
        joystick.update();
        player.update();

        if(player.getYLevel() == 0 && !filledOxygen)
        {
            oxygenFill.start();
            filledOxygen = true;
        }
        else if(player.getYLevel() > 0)
        {
            filledOxygen = false;
        }

        shopButton.setShow(player.getYLevel() == 0);
    }//end update

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null)    {
            // if our canvas exists then we can use it - not pointing to a null

            // save current state of our canvas
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX * scaleFactorXMul, scaleFactorY * scaleFactorYMul);
            if(!playerIsDead) {
                canvas.drawColor(Color.rgb(90, 185, 255));
                boat.draw(canvas);
                mapGen.draw(canvas, player);
                player.draw(canvas, player.getPlayerRectLeft(), player.getPlayerRectTop(), joystick.getAngle(), player.getCenterPosX(), player.getCenterPosY());

                player.oxygenBar.draw(canvas);
                joystick.draw(canvas);
                mineButton.draw(canvas);

                shop.draw(canvas);
                shopButton.draw(canvas);

                if(shop.isShow())
                {
                    speedBoostBtn.draw(canvas);
                    oxygenBoostBtn.draw(canvas);
                    goldBoostBtn.draw(canvas);

                    canvas.drawText("" + speedBoostPrice, 350, 557, pricePaint);
                    canvas.drawText("" + oxygenBoostPrice, 780, 557, pricePaint);
                    canvas.drawText("" + goldBoostPrice, 1205, 557, pricePaint);

                    if(player.goldMultiplier == 2)
                    {
                        canvas.drawText("SOLD OUT!", 1310, 600, depthPaint);
                    }
                    if(player.getSpeed() == player.getMaxSpeed())
                    {
                        canvas.drawText("SOLD OUT!", 475, 600, depthPaint);
                    }
                    if(player.oxygenBar.getTimeToEmpty() == player.oxygenBar.getMaxOxygenSeconds())
                    {
                        canvas.drawText("SOLD OUT!", 895, 600, depthPaint);
                    }
                }


                canvas.drawText("Depth: " + player.getDepth() + "m", 400, 250, depthPaint);
                canvas.drawText("" + player.getGoldAmount(), WIDTH + 350, 170, goldPaint);
                goldUI.draw(canvas);

                if (player.getCurrentOxygen() <= 0) //if oxygen depleted, start fainting
                {
                    vignetteRadius -= 50;

                    if (vignetteRadius <= 1) {
                        vignetteRadius = 1;
                        //player dead!
                        playerIsDead = true;
                    }
                    vignette(canvas, vignetteRadius);
                    canvas.drawRect(vignetteRect, vignettePaint);
                } else if (player.getYLevel() == 0)//if player is at surface level, slowly remove vignette
                {
                    if (vignetteRadius < 5000) {
                        vignetteRadius += 100;
                        vignette(canvas, vignetteRadius);
                        canvas.drawRect(vignetteRect, vignettePaint);
                    }
                } else if (vignetteRadius < 5000)//if player has breathed for a moment and returned to water, continue removing the effect
                {
                    vignetteRadius += 100;
                    vignette(canvas, vignetteRadius);
                    canvas.drawRect(vignetteRect, vignettePaint);
                }
            }
            else //if player is dead
            {
                canvas.drawColor(Color.BLACK);
                canvas.drawText("You Died!", 670, 450, deathPaint);
                canvas.drawText("Gold Mined: " + player.getMaxGold(), 700, 600, scorePaint);
                canvas.drawText("Deepest Dive: " + player.getMaxDepth() + "m", 700, 700, scorePaint);

                if(!gameEnded)
                {
                    gameEnded = true;
                    EndGameThread endGameThread = new EndGameThread(4000);
                    endGameThread.start();

                    //save results
                    saveScore(playerName, player.getMaxGold(), player.getMaxDepth());
                }
            }

            // restore the saves canvas state back
            canvas.restoreToCount(savedState);
        }
    }//end draw

    private void saveScore(String playerName, int goldMined, int deepestDive)
    {
        SharedPreferences prefs = getContext().getSharedPreferences(ShowScoresActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String scoresJson = prefs.getString(ShowScoresActivity.SCORES_KEY, "[]");
        Moshi moshi = new Moshi.Builder().build();
        Type listOfScoresType = Types.newParameterizedType(List.class, StoredScore.class);
        JsonAdapter<List<StoredScore>> adapter = moshi.adapter(listOfScoresType);
        List<StoredScore> scores;

        //add new score to the list and store the list, remove last score if there are more than 10
        try {
            scores = adapter.fromJson(scoresJson);
            if(scores == null)
                scores = new ArrayList<>();
        }catch (IOException e){
            scores = new ArrayList<>();
        }

        //add the new score to the list
        scores.add(new StoredScore(playerName, goldMined, deepestDive));

        //sort
        BubbleSort sorter = new BubbleSort(new ArrayList<>(scores));
        sorter.bubbleSort();
        scores = sorter.getSortedListViewItems();

        if(scores.size() > 10)
            scores = scores.subList(0, 10);

        scoresJson = adapter.toJson(scores);
        editor.putString(ShowScoresActivity.SCORES_KEY, scoresJson);
        editor.apply();
    }

    public void vignette(Canvas canvas, float r) {
        float radius = r;

        RadialGradient gradient = new RadialGradient((int)((canvas.getWidth() * 1.35)/2), (int)(canvas.getHeight() * 0.775), radius, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);

        vignettePaint = new Paint();
        vignettePaint.setColor(Color.BLACK);
        vignettePaint.setShader(gradient);

        vignetteRect = new Rect(0, 0, (int)(canvas.getWidth() * 1.35), (int)(canvas.getHeight() * 1.5));
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}//end of class
