package com.example.divedeep;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Button extends AnimatedSpritesObject
{
    private float buttonRadius;
    private float centerX;
    private float centerY;
    public float distanceToTouch;
    Rect buttonRect;

    public Button(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength, float x, float y, boolean visible) {
        super(imageThatHasSprites, numberOfSprites, rowLength);

        this.x = (int)x;
        this.y = (int)y;

        buttonRadius = getWidth() / 2;

        centerX = x + getWidth() / 2;
        centerY = y + getHeight() / 2;

        buttonRect = getRectangle();

        setShow(visible);
    }

    public Button(int left, int top, int right, int bottom)
    {
        super(null, 0, 0);

        this.x = (int)left;
        this.y = (int)top;

        buttonRect = new Rect(left, top, right, bottom);

        centerX = left + (right - left) / 2;
        centerY = top + (bottom - top) / 2;
    }

    public boolean isPressedRadius(float touchX, float touchY)
    {
        if(!isShow())
            return false;

        distanceToTouch = (float)Math.sqrt(Math.pow(touchY - centerY, 2) + Math.pow(touchX - centerX, 2));

        return distanceToTouch <= buttonRadius;
    }

    public boolean isPressedRect(float touchX, float touchY)
    {
        if(!isShow())
            return false;

        return (touchX >= buttonRect.left && touchX <= (buttonRect.left + getWidth()) &&
                touchY >= buttonRect.top && touchY <= (buttonRect.top + getHeight()));
    }

    public float getCenterX()
    {
        return centerX;
    }
    public float getCenterY()
    {
        return centerY;
    }
}
