package com.example.divedeep;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Gold extends AnimatedSpritesObject
{
    private int rotationAngle;
    private int startX;
    private int startY;
    private int goldSize;

    public Gold(int x, int y, int rotationAngle, int goldSize) {
        super(getBitmapForSize(goldSize), 1, 1);

        startX = x;
        startY = y;

        this.rotationAngle = rotationAngle;

        this.goldSize = goldSize;
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
    private static Bitmap getBitmapForSize(int goldSize) {
        switch (goldSize) {
            case 0:
                return BitmapFactory.decodeResource(GamePanel.res, R.drawable.goldsmall);
            case 1:
                return BitmapFactory.decodeResource(GamePanel.res, R.drawable.goldmedium);
            case 2:
                return BitmapFactory.decodeResource(GamePanel.res, R.drawable.goldlarge);
            default:
                return BitmapFactory.decodeResource(GamePanel.res, R.drawable.goldlarge); // Default to large
        }
    }
    public int getRotationAngle()
    {
        return rotationAngle;
    }
    public int getSize()
    {
        return goldSize;
    }
}
