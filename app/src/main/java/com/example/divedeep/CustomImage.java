package com.example.divedeep;

import android.graphics.Bitmap;

public class CustomImage extends AnimatedSpritesObject
{
    MapGenerator map;

    public CustomImage(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength, MapGenerator map) {
        super(imageThatHasSprites, numberOfSprites, rowLength);

        this.map = map;
    }

    public CustomImage(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength) {
        super(imageThatHasSprites, numberOfSprites, rowLength);
    }

    public CustomImage(Bitmap imageThatHasSprites, int numberOfSprites, int rowLength, int centerX, int centerY) {
        super(imageThatHasSprites, numberOfSprites, rowLength);

        this.x = centerX - getWidth() / 2;
        this.y = centerY - getHeight() / 2;
    }

    @Override
    public void update()
    {
        super.update();
        if(map != null) {
            x = (int) (map.xOffset + map.cellSize * map.mapWidth / 2) - getWidth() / 2;
            y = (int) (map.yOffset - getHeight() * 0.92f);
        }
    }
}
