package com.example.divedeep;

import android.graphics.Bitmap;

public class SeaUrchin extends AnimatedSpritesObject
{
    private int rotationAngle;
    private int startX;
    private int startY;

    public SeaUrchin(Bitmap image, int x, int y, int rotationAngle) {
        super(image, 1, 1);

        startX = x;
        startY = y;

        this.rotationAngle = rotationAngle;
    }

    public void update()
    {
        super.update();
        if(rotationAngle == 0)
        {
            x = (int)(startX - getWidth() / 2 + MapGenerator.xOffset);
            y = (int)(startY - getHeight() * 0.75f + MapGenerator.yOffset);
        }
        else if(rotationAngle == 90)
        {
            x = (int)(startX - getWidth() * 0.25f + MapGenerator.xOffset);
            y = (int)(startY - getHeight() / 2 + MapGenerator.yOffset);
        }
        else
        {
            x = (int)(startX - getWidth() * 0.75f + MapGenerator.xOffset);
            y = (int)(startY - getHeight() / 2 + MapGenerator.yOffset);
        }
    }

    public int getRotationAngle()
    {
        return rotationAngle;
    }
}
