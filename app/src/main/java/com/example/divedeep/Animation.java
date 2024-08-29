package com.example.divedeep;

import android.graphics.Bitmap;

/**
 * @author Created by Eli Guriel on 22/03/2021.
 * @version version 2.00
 * @since version 2.00
 * Study Android,
 * Modi-in, YACHAD high-school.
 *
 * *****************************************************************
 * <p>
 * Class description:
 * The Animation class will help
 * us to animate the images
 * through their sprites...
 * *************************************************************
 */

public class Animation {
    // a bitmap table to keep the numbers of the image sprites [0]Sprite [1] sprite [2]sprite...[x] sprite
    private Bitmap[] sprites;

    // sprite of the image we are currently
    private int currentSprite;

    // Witch Sprite row of the image we are currently
    private int spriteRow;

    // each row has number of Sprites
    private int rowLength;

    // Timer for the animation - start time
    private long startTime;

    // and a delay (is the delay between the sprites *How fast our animation is gonna be*)
    private long delay;

    // we need also a boolean for the images that are going to animate once in our screen
    // for example the explosion image we want to happened once
    // init to TRUE
    private boolean playedOnce = true;


    /**
    // in setSprites we create an animation we need the sprites of an image
    // in witch Sprite we are.. and a timer
     */
    public void setSprites(Bitmap[] sprites) {
        // we get the Sprites images
        this.sprites = sprites;

        // every image will start from the 1st sprite
        currentSprite = spriteRow * rowLength;

        // we set the time of our animation to our systems timer
        startTime = System.nanoTime();
    }//end setSprites

    //update method
    public void update() {
        /**
         * timer determines witch Sprite of the image is going be return every time
         * In general we use Timers in our program to determine what time an object will appear
         * in our screen and in the meantime what actions will do
         * All objects have a timer...
         */
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        /**
         * Now if we set the delay of the animation to xxx millis
         * we want to start the animation after xxx millis when we start the game.
         * beCare, if we set big delay then our animation is going be very slow
         */
        if (elapsed > delay) {
            currentSprite++;
            startTime = System.nanoTime();
        }
        if (currentSprite == rowLength + (spriteRow * rowLength) - 1) {
            /** when we animate the all row, then set playOnce to false
             we can use it in the draw method of the class as a boolean condition
             */
            playedOnce = true;
        }
        // start again from row start
        if (currentSprite == rowLength + (spriteRow * rowLength)) {
            currentSprite = spriteRow * rowLength;

        }
    }//end update

    /**
     * Setters and Getters ***************************************************************
     */

    public Bitmap getImage() {
        return sprites[currentSprite];
    }

    // these two methods used in game object that shown once, like the explosion
    public boolean getPlayedOnce() {
        return playedOnce;
    }

    public void setPlayedOnce(boolean playedOnce) {
        this.playedOnce = playedOnce;
    }
    /**
     * set the row of the sprites (png file) image
     */
    public void setSpriteRow(int SpriteRow) {

        this.spriteRow = SpriteRow;
        currentSprite = SpriteRow * rowLength;
    }

    // set the row length of the image
    public void setRowLength(int rowLength) {

        this.rowLength = rowLength;
    }

    // Delay of the Sprites
    public void setDelay(long delay) {
        this.delay = delay;
    }

    // setter for starting not from the first Sprite
    public void setSprite(int currentSprite) {
        this.currentSprite = currentSprite;
    }

    // TODO 3 add setters and getters to animation for rolling dice use


    public Bitmap[] getSprites() {
        return sprites;
    }

    public int getRowLength() {
        return rowLength;
    }

    public int getSpriteRow() {
        return spriteRow;
    }

    public boolean isPlayedOnce() {
        return playedOnce;
    }

    public int getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(int currentSprite) {
        this.currentSprite = currentSprite;
    }
}//end class
