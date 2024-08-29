package com.example.divedeep;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;


/**
 * @author Created by Eli Guriel on 22/03/2021.
 * @version version 2.00
 * @since version 2.00
 * Study Android,
 * Modi-in, YACHAD high-school.
 *
 * *************************************************************
 * this class extends the GameObject class
 * and adds:
 * Animation class as variable object for the objects animations
 * *************************************************************
 */

public class AnimatedSpritesObject extends GameObject {

    // show flag - set default true, so we can see the image
    private boolean show = true;

    // this class uses the Animation class, so we make a reference to a new Animation
    protected Animation animation = new Animation();

    // set an array of bitmaps for our sprites
    Bitmap[] spritesArray;
    // save bitmap  - this is used for rotate method
    Bitmap[] saveSpritesArray;

    // bitmap image from where we cut our sprites
    private Bitmap spriteSheet;

    // start time pointer, for many uses
    private long startTime;

    /** So to create an animated object
     * first we pass the bit map image (the image that has the sprites) - imageThatHasSprites
     * the dimensions of each sprite we get from the bitmap image
     * numberOfSprites -  number Of Sprites in the all image
     * rowLength - number of sprites in each row
     * so now we can cut each sprite from the image and save it in an array of bitmap (spritesArray)
     * and pass this array to the animation class
     */
    public AnimatedSpritesObject(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength) {

        /**
         * first calculate:
         * The dimensions of each sprite in the image - width and height -
         * from rowLength and numberOfSprites
         * so we can create a bitmap
         * for each sprite
         */
        if(imageThatHasSprites != null)
        {
            height = imageThatHasSprites.getHeight() /(numberOfSprites/rowLength);
            width = imageThatHasSprites.getWidth() / rowLength;

            // set an array of bitmaps for our sprites
            spritesArray = new Bitmap[numberOfSprites];

            int row = 0;

            //loop
            for (int i = 0; i < spritesArray.length; i++) {
                /**
                 *Our image has XxX sprites so [0] is the first sprite,  [1] the second,  [2] the third ...
                 */
                if (i % rowLength == 0 && i > 0) row++;
                spritesArray[i] = Bitmap.createBitmap(imageThatHasSprites, (i - (rowLength * row)) * width, row * height,
                        width, height);
            }


            // now that we know all the info about the bitmap image
            // we need to set the animation and the delay

            // init to row 0
            animation.setSpriteRow(0);
            animation.setRowLength(rowLength);
            // pass the sprites array
            animation.setSprites(spritesArray);
            saveSpritesArray = spritesArray;
            animation.setDelay(100);

            //Now we initiate the timer so we can use in the update method
            startTime = System.nanoTime();
        }
    }//end constructor

    // update method
    public void update() {
        // here is the timer of our player millis
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        // Now when our timer gets past xxx millis we want the to do something
        if (elapsed > 100) {
            // here is our code


            // set back start time
            startTime = System.nanoTime();
        }
        // update the animation
        animation.update();

    }//end update


    /**
     * draw our object on the canvas (screen)
     */
    public void draw(Canvas canvas) {
        if (show) canvas.drawBitmap(animation.getImage(), x, y, null);
    }//end draw

    public void draw(Canvas canvas, float x, float y) {
        if (show) canvas.drawBitmap(animation.getImage(), x, y, null);
    }//end draw

    public void draw(Canvas canvas, float rotationAngle, float px, float py) {
        if (show)
        {
            canvas.save();
            canvas.rotate(rotationAngle, px, py);
            canvas.drawBitmap(animation.getImage(), x, y, null);
            canvas.restore();
        }
    }
    public void draw(Canvas canvas, float rotationAngle, float px, float py, Paint paint) {
        if (show)
        {
            canvas.save();
            canvas.rotate(rotationAngle, px, py);
            canvas.drawBitmap(animation.getImage(), x, y, paint);
            canvas.restore();
        }
    }

    public void draw(Canvas canvas, float x, float y, float rotationAngle, float px, float py) {
        if (show)
        {
            canvas.save();
            canvas.rotate(rotationAngle, px, py);
            canvas.drawBitmap(animation.getImage(), x, y, null);
            canvas.restore();
        }
    }//end draw

    // this will flip the image - horizontally if True, Vertically if False
    public void flipImage(boolean horizontally, boolean vertically) {
        Bitmap[] bOutput = new Bitmap[spritesArray.length];
        Matrix matrix = new Matrix();

        if (horizontally && !vertically)
            matrix.postScale(-1f, 1f);
        else if(vertically && !horizontally)
            matrix.postScale(1f, -1f);
        else if(horizontally && vertically)
            matrix.postScale(-1f, -1f);

        for (int i = 0; i < spritesArray.length; i++) {
            bOutput[i] = Bitmap.createBitmap(spritesArray[i], 0, 0, spritesArray[i].getWidth(),
                    spritesArray[i].getHeight(), matrix, true);
        }
        spritesArray = bOutput;
        animation.setSprites(spritesArray);
    }

    /**
     * Setters and Getters *******************************************************************
     */
    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    // this will retrieve the current sprite image
    public Bitmap getImage() {
        return animation.getImage();
    }

    public void setNewBitmap(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength)
    {
        height = imageThatHasSprites.getHeight() /(numberOfSprites/rowLength);
        width = imageThatHasSprites.getWidth() / rowLength;

        spritesArray = new Bitmap[numberOfSprites];

        int row = 0;

        for (int i = 0; i < spritesArray.length; i++) {
            if (i % rowLength == 0 && i > 0) row++;
            spritesArray[i] = Bitmap.createBitmap(imageThatHasSprites, (i - (rowLength * row)) * width, row * height,
                    width, height);
        }

        animation.setSpriteRow(0);
        animation.setRowLength(rowLength);
        // pass the sprites array
        animation.setSprites(spritesArray);
        saveSpritesArray = spritesArray;
        animation.setDelay(100);
    }
}
