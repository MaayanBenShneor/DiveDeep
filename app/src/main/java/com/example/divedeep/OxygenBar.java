package com.example.divedeep;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.divedeep.AnimatedSpritesObject;

public class OxygenBar extends AnimatedSpritesObject
{
    public static int MAX_OXYGEN_SECONDS = 100;
    public float maxOxygen = 100;
    public float currentOxygen = maxOxygen;
    public float timeToEmpty = 40; //in seconds
    private float oxygenConsumptionRate = maxOxygen / timeToEmpty / 30; //30 = updates per second
    private OxygenArrow arrow;
    public boolean fillingOxygen;
    public OxygenBar(Bitmap gaugeSprite, Bitmap arrowSprite, int numberOfSprites, int rowLength) {
        super(gaugeSprite, numberOfSprites, rowLength);

        x = 100;
        y = 100;

        arrow = new OxygenArrow(arrowSprite, 1, 1);
    }

    @Override
    public void update()
    {
        if(fillingOxygen) {
            if (currentOxygen < maxOxygen)
                currentOxygen += maxOxygen / 40;
            else
                currentOxygen = maxOxygen;
        }

        //consume oxygen
        if(!fillingOxygen)
            currentOxygen = Math.max(0, currentOxygen - (oxygenConsumptionRate)); //Math.max to make sure oxygen is not negative
        //update the arrow's angle
        float angle = arrow.calculateAngle(currentOxygen, maxOxygen);
        arrow.setAngle(angle);
    }
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        arrow.draw(canvas, x, y, arrow.getAngle(), x + arrow.getWidth() / 2, y + arrow.getHeight() / 2);
    }
    public void changeTimeToEmpty(int newTime)
    {
        timeToEmpty = Math.min(MAX_OXYGEN_SECONDS, newTime);
        oxygenConsumptionRate = maxOxygen / timeToEmpty / 30; //30 = avg updates per second
    }

    public int getMaxOxygenSeconds()
    {
        return MAX_OXYGEN_SECONDS;
    }

    public float getTimeToEmpty() {
        return timeToEmpty;
    }
}

/**
 * Arrow class
 */

class OxygenArrow extends AnimatedSpritesObject
{
    private float minAngle = -130;
    private float maxAngle = 130;
    private float angle;
    public OxygenArrow(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength) {
        super(imageThatHasSprites, numberOfSprites, rowLength);
    }
    public float calculateAngle(float currentOxygen, float maxOxygen) {

        //the percentage of the oxygen remaining
        float oxygenPercentage = currentOxygen / maxOxygen;

        //map the percentage to the angle range [minAngle, maxAngle] and return
        return minAngle + (maxAngle - minAngle) * oxygenPercentage;
    }
    public float getAngle()
    {
        return angle;
    }
    public void setAngle(float angle)
    {
        this.angle = angle;
    }
}
