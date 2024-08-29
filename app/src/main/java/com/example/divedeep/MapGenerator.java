package com.example.divedeep;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
    private final int[][] map;
    public final int mapWidth;
    public final int mapHeight;
    private final int randomFillPercent = 49;
    private final int smoothingIterations = 5;
    public final int cellSize = 30;
    private final Paint paint;
    public int wallColor;
    private int waterColor;
    public static float xOffset;
    public static float yOffset;
    private float startingOffsetX = 2920;
    private float startingOffsetY = -1575;
    private int goldChance = 4;
    public List<Gold> goldList = new ArrayList<>();
    private int urchinChance = 2;
    public List<SeaUrchin> urchinList = new ArrayList<>();

    public MapGenerator(int w, int h)
    {
        mapWidth = w;
        mapHeight = h;

        paint = new Paint();

        map = new int[mapWidth][mapHeight];

        RandomFillMap();

        for(int i = 0; i < smoothingIterations; i++)
        {
            smoothMap();
        }

        generateGold();

        generateUrchins();
    }

    //fill the map randomly with either 0 or 1
    //the randomFillPercent is how likely the value should be 1
    private void RandomFillMap() {
        Random randGen = new Random();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                // Make sure the edges are always full and leave a hole at the top middle
                if(y <= 20 && (x <= (mapWidth / 2) + 25 && x >= (mapWidth / 2) - 25))
                    map[x][y] = 0;
                else if (x <= 35 || x >= mapWidth - 35 || y == 0 || y >= mapHeight - 18) {
                    map[x][y] = 1;
                } else {
                    map[x][y] = (randGen.nextInt(101) < randomFillPercent) ? 1 : 0;
                }
            }
        }
    }

    //this is where we use the cellular automata algorithm to give the random mess a natural shape
    public void smoothMap()
    {
        for(int x = 0; x < mapWidth; x++)
        {
            for (int y = 0; y < mapHeight; y++)
            {
                int neighborWallTiles = getSurroundingWallCount(x, y);

                //if the current cell has more than 4 full neighbors, set it to full.
                if(neighborWallTiles > 4)
                    map[x][y] = 1;
                //else, if it has less than 4 set to 0
                else if(neighborWallTiles < 4)
                    map[x][y] = 0;
                //otherwise keep it as it was
            }
        }
    }

    private int getSurroundingWallCount(int gridX, int gridY)
    {
        int wallCount = 0;

        //looping on a 3x3 grid around the given cell (gridX, gridY)
        for(int neighborX = gridX -1; neighborX <= gridX +1; neighborX++)
        {
            for(int neighborY = gridY -1; neighborY <= gridY +1; neighborY++)
            {
                //if the current neighbor we are looking at is not outside of the map
                if(neighborX >= 0 && neighborX < mapWidth && neighborY >= 0 && neighborY < mapHeight)
                {
                    //if the current neighbor is not the cell we are checking the neighbors for
                    if(neighborX != gridX || neighborY != gridY)
                    {
                        //add to the counter the neighbor (if the wall is full it will add a 1 and 0 otherwise)
                        wallCount += map[neighborX][neighborY];
                    }
                }
                else //if the current neighbor we are looking at is outside the map, consider it as full and add to the counter
                    wallCount++;
            }
        }

        return wallCount;
    }

    private void generateUrchins()
    {
        Random randGen = new Random();

        for (int i = 0; i < mapWidth - 1; i++) {
            for (int j = 0; j < mapHeight - 1; j++) {
                int state = getState(map[i][j], map[i + 1][j], map[i + 1][j + 1], map[i][j + 1]);

                if (randGen.nextInt(101) < urchinChance) {
                    if (state == 3) //gold is on the floor
                        urchinList.add(new SeaUrchin(BitmapFactory.decodeResource(GamePanel.res, R.drawable.seaurchin),i * cellSize + cellSize, j * cellSize + cellSize / 2, 0));
                    else if (state == 6) //gold is on wall, rotate -90 degrees
                        urchinList.add(new SeaUrchin(BitmapFactory.decodeResource(GamePanel.res, R.drawable.seaurchin), i * cellSize + cellSize, j * cellSize + cellSize, -90));
                    else if (state == 9) //gold is on wall, rotate 90 degrees
                        urchinList.add(new SeaUrchin(BitmapFactory.decodeResource(GamePanel.res, R.drawable.seaurchin), i * cellSize + cellSize, j * cellSize + cellSize, 90));
                }
            }
        }
    }

    private void generateGold() {
        Random randGen = new Random();

        for (int i = 0; i < mapWidth - 1; i++) {
            for (int j = 0; j < mapHeight - 1; j++) {
                int state = getState(map[i][j], map[i + 1][j], map[i + 1][j + 1], map[i][j + 1]);

                // Calculate the gold size based on yLevel and probability
                int goldSize;
                double randomValue = randGen.nextDouble();

                if (j < mapHeight / 3) {
                    // Higher chance for small gold
                    if (randomValue < 0.6) {
                        goldSize = 0; // Small gold
                    } else {
                        goldSize = 1; // Medium gold
                    }
                } else if (j < 2 * mapHeight / 3) {
                    // Higher chance for medium gold
                    if (randomValue < 0.6) {
                        goldSize = 1; // Medium gold
                    } else {
                        goldSize = 2; // Large gold
                    }
                } else {
                    // Higher chance for large gold
                    if (randomValue < 0.6) {
                        goldSize = 2; // Large gold
                    } else {
                        goldSize = 1; // Medium gold
                    }
                }

                if (randGen.nextInt(101) < goldChance) {
                    if (state == 3) //gold is on the floor
                        goldList.add(new Gold(i * cellSize + cellSize, j * cellSize + cellSize, 0, goldSize));
                    else if (state == 6) //gold is on wall, rotate -90 degrees
                        goldList.add(new Gold(i * cellSize + cellSize, j * cellSize + cellSize, -90, goldSize));
                    else if (state == 9) //gold is on wall, rotate 90 degrees
                        goldList.add(new Gold(i * cellSize + cellSize, j * cellSize + cellSize, 90, goldSize));
                }
            }
        }
    }

    public void draw(Canvas canvas, Player player)
    {
        //get the dimensions of the screen
        float screenCenterX = canvas.getWidth() / 2;
        float screenCenterY = canvas.getHeight() / 2;

        //calculate the new offset for the map after the player has moved from the starting point
        float newOffsetX = -(float)player.getPosX() - screenCenterX;
        float newOffsetY = -(float)player.getPosY() - screenCenterY;

        //add the offset after the player has moved to the starting position of the map
        xOffset = newOffsetX - startingOffsetX;
        yOffset = newOffsetY - startingOffsetY;

        Rect clipBounds = canvas.getClipBounds();
        //calculate the start and end indexes for the visible region
        int startX = Math.max((int)((clipBounds.left - xOffset) / cellSize), 0);
        int endX = Math.min((int)((clipBounds.right - xOffset) / cellSize) + 1, mapWidth);
        int startY = Math.max((int)((clipBounds.top - yOffset) / cellSize), 0);
        int endY = Math.min((int)((clipBounds.bottom - yOffset) / cellSize) + 1, mapHeight);

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                //the position of the current block on the screen
                float blockLeft = i * cellSize + xOffset;
                float blockTop = j * cellSize + yOffset;
                float blockRight = blockLeft + cellSize;
                float blockBottom = blockTop + cellSize;

                //check if square is at least partially visible on the screen
                if (visibleOnScreen(canvas, (int)blockLeft, (int)blockTop, (int)blockRight, (int)blockBottom))
                {
                    //if a wall
                    if (map[i][j] == 1)
                    {
                        if(j == 0)
                        {
                            //invisible wall to stop the player from swimming above water level
                            player.checkCollisionWithWall(blockLeft, blockTop -cellSize, cellSize);
                        }

                        //check if currently colliding with the player
                        player.checkCollisionWithWall(blockLeft, blockTop, cellSize);
                        // Interpolate between light brown and dark brown based on Y-coordinate
                        wallColor = interpolateColor(Color.rgb(204, 102, 0), Color.rgb(51, 25,0), (float)j / mapHeight);
                        paint.setColor(wallColor);
                    }
                    else {
                        if(j == 0)
                        {
                            //invisible wall to stop the player from swimming above water level but still gives him room to breath
                            player.checkCollisionWithWall(blockLeft, blockTop -cellSize*2, cellSize);
                            waterColor = Color.argb(220, 0, 128, 245);
                            paint.setColor(waterColor);
                        }
                        else
                        {
                            // Interpolate between light blue and dark blue based on Y-coordinate
                            waterColor = interpolateColor(Color.rgb(0, 128, 255), Color.rgb(0, 25, 51), (float) j / mapHeight);
                            paint.setColor(waterColor);
                        }
                    }

                    canvas.drawRect(blockLeft, blockTop, blockRight, blockBottom, paint);

                    //keep track of the y of the player
                    if(player.getHeadPosY() <= blockBottom && player.getHeadPosY() >= blockTop)
                        player.setYLevel(j);
                }
            }
        }

        List<Gold> visibleGoldList = new ArrayList<>();

        //draw only the gold chunks that are visible on the screen at the moment
        for(Gold gold : goldList)
        {
            gold.update();

            if(visibleOnScreen(canvas, gold.getRectangle()))
            {
                visibleGoldList.add(gold);

                gold.draw(canvas, gold.getRotationAngle(), gold.getX() + gold.getWidth()/2, gold.getY() + gold.getHeight()/2);
            }

            player.checkGold(visibleGoldList);
        }

        List<SeaUrchin> visibleUrchins = new ArrayList<>();

        //draw only the gold chunks that are visible on the screen at the moment
        for(SeaUrchin urchin : urchinList)
        {
            urchin.update();

            if(visibleOnScreen(canvas, urchin.getRectangle()))
            {
                visibleUrchins.add(urchin);

                urchin.draw(canvas, urchin.getRotationAngle(), urchin.getX() + urchin.getWidth()/2, urchin.getY() + urchin.getHeight()/2);
            }

            player.checkUrchin(visibleUrchins);
        }
    }

    private boolean visibleOnScreen(Canvas canvas, int left, int top, int right, int bottom)
    {
        Rect clipBounds = canvas.getClipBounds();
        return (right >= clipBounds.left && left <= clipBounds.right && bottom >= clipBounds.top && top <= clipBounds.bottom);
    }

    private boolean visibleOnScreen(Canvas canvas, Rect rect)
    {
        Rect clipBounds = canvas.getClipBounds();
        return (rect.right >= clipBounds.left && rect.left <= clipBounds.right && rect.bottom >= clipBounds.top && rect.top <= clipBounds.bottom);
    }


    private int interpolateColor(int color1, int color2, float t) {
        float[] hsvColor1 = new float[3];
        float[] hsvColor2 = new float[3];

        Color.colorToHSV(color1, hsvColor1);
        Color.colorToHSV(color2, hsvColor2);

        for (int i = 0; i < 3; i++) {
            hsvColor1[i] = (1 - t) * hsvColor1[i] + t * hsvColor2[i];
        }

        return Color.HSVToColor(hsvColor1);
    }

    private int getState(int a, int b, int c, int d) {
        return a * 8 + b * 4 + c * 2 + d * 1;
    }
}
