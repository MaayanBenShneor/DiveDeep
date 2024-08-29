package com.example.divedeep;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.List;

public class Player extends AnimatedSpritesObject {
    private double dirX;
    private double dirY;
    private float speed = 10;
    private float MAX_SPEED = 20;
    private double positionX;
    private double positionY;
    private int yLevel = 0;
    private float playerRectLeft;
    private float playerRectTop;
    private boolean facingRight = true;
    private float collisionRadius = 45;
    public OxygenBar oxygenBar;
    private GamePanel gamePanel;
    private Joystick joystick;
    private Gold goldToMine;
    public float distanceToUrchin;
    private int goldAmount;
    private int maxGold;
    private int depth;
    private int maxDepth;
    public int goldMultiplier = 1;
    public Player(GamePanel gamePanel, Joystick joystick, Bitmap imageThatHasSprites, int numOfSprites, int rowLength) {
        super(imageThatHasSprites, numOfSprites, rowLength);


        this.gamePanel = gamePanel;
        this.joystick = joystick;

        positionX = (gamePanel.WIDTH / 2 / gamePanel.scaleFactorXMul);
        positionY = gamePanel.HEIGHT / 2 / gamePanel.scaleFactorYMul;

        playerRectLeft = (float)positionX - getWidth() / 2;
        playerRectTop = (float)positionY - getHeight() / 2;

        oxygenBar = new OxygenBar(BitmapFactory.decodeResource(gamePanel.res, R.drawable.oxygengauge), BitmapFactory.decodeResource(gamePanel.res, R.drawable.oxygenarrow), 1, 1);
    }

    public void update() {
        super.update();

        if (joystick.getIsPressed()) {
            dirX = joystick.getDirX();
            dirY = joystick.getDirY();

            positionX += dirX * speed;
            positionY += dirY * speed;

            if (dirX < 0 && facingRight) {
                flipImage(false, true);
                facingRight = false;
            } else if (dirX > 0 && !facingRight) {
                flipImage(false, true);
                facingRight = true;
            }
        }

        depth = (int)(yLevel * 0.5f);

        if(depth > maxDepth)
            maxDepth = depth;

        fillOxygen(depth == 0);
        oxygenBar.update();
    }

    @Override
    public void draw(Canvas canvas, float x, float y, float rotationAngle, float px, float py)
    {
        super.draw(canvas, x, y, rotationAngle, px, py);
    }

    public boolean checkCollisionWithWall(float wallX, float wallY, int cellSize) {
        float wallCenterX = wallX + (cellSize / 2);
        float wallCenterY = wallY + (cellSize / 2);

        //calculate the distance between the player's head and the center of the wall
        float distance = (float) Math.sqrt(Math.pow(getHeadPosX() - wallCenterX, 2) + Math.pow(getHeadPosY() - wallCenterY, 2));

        if (distance <= collisionRadius) {
            //calculate the direction vector from the player to the wall
            float dirToWallX = getHeadPosX() - wallCenterX;
            float dirToWallY = getHeadPosY() - wallCenterY;

            //normalize the vector
            float length = (float) Math.sqrt(dirToWallX * dirToWallX + dirToWallY * dirToWallY);
            dirToWallX /= length;
            dirToWallY /= length;

            //calculate the overlap distance
            float overlap = collisionRadius - distance;

            //adjust the player's position based on the collision normal and overlap
            positionX += dirToWallX * overlap;
            positionY += dirToWallY * overlap;

            return true;
        }

        return false;
    }

    public void checkGold(List<Gold> goldList)
    {
        boolean foundGold = false;

        for(Gold gold : goldList)
        {
            float distanceToGold = (float)Math.sqrt(Math.pow(gold.getY() + gold.getHeight() / 2 - getHeadPosY(), 2) + Math.pow(gold.getX() + gold.getWidth() / 2 - getHeadPosX(), 2));

            if(distanceToGold <= 150 && gold.isShow())
            {
                foundGold = true;
                goldToMine = gold;
            }
        }

        gamePanel.mineButton.setShow(foundGold);
    }

    public void checkUrchin(List<SeaUrchin> urchinList)
    {
        for(SeaUrchin urchin : urchinList)
        {
            distanceToUrchin = (float)Math.sqrt(Math.pow(urchin.getY() + urchin.getHeight() / 2 - getHeadPosY(), 2) + Math.pow(urchin.getX() + urchin.getWidth() / 2 - getHeadPosX(), 2));

            if(distanceToUrchin <= 50)
            {
                oxygenBar.currentOxygen -= 0.05;
            }
        }
    }
    public void mineGold()
    {
        goldToMine.setShow(false);
        goldAmount += (goldToMine.getSize() + 1) * goldMultiplier;
        maxGold += (goldToMine.getSize() + 1) * goldMultiplier; //the goldAmount variable will change when purchasing items. the maxGold wont
    }
    public void fillOxygen(boolean fill)
    {
        oxygenBar.fillingOxygen = fill;
    }

    public float getCenterPosX()
    {
        return (float)(playerRectLeft + getWidth() * 0.5);
    }

    public float getCenterPosY()
    {
        return (float)(playerRectTop + getHeight() * 0.5);
    }

    public float getPlayerRectLeft()
    {
        return playerRectLeft;
    }

    public float getPlayerRectTop()
    {
        return playerRectTop;
    }
    public double getPosX()
    {
        return positionX;
    }

    public double getPosY()
    {
        return positionY;
    }
    public float getHeadPosX()
    {
        return getCenterPosX() + (float)joystick.getNormalizedDirX() * 55;
    }
    public float getHeadPosY()
    {
        return getCenterPosY() + (float)joystick.getNormalizedDirY() * 55;
    }
    public void setYLevel(int y)
    {
        yLevel = y;
    }
    public int getYLevel()
    {
        return yLevel;
    }

    public int getGoldAmount() {
        return goldAmount;
    }
    public int getMaxGold()
    {
        return maxGold;
    }
    public void spendGold(int amount)
    {
        if(goldAmount >= amount)
            goldAmount -= amount;
    }
    public float getCurrentOxygen()
    {
        return oxygenBar.currentOxygen;
    }

    public void mulSpeed(float mul)
    {
        speed = Math.min(speed *= mul, MAX_SPEED);
    }

    public float getSpeed()
    {
        return speed;
    }

    public int getDepth() {
        return depth;
    }
    public int getMaxDepth() {
        return maxDepth;
    }

    public float getMaxSpeed()
    {
        return MAX_SPEED;
    }
}
