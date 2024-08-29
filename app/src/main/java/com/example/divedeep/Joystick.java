package com.example.divedeep;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Joystick {
    private final int outerCircleX;
    private final int outerCircleY;
    private int innerCircleX;
    private int innerCircleY;

    private final Paint innerCirclePaint;
    private final Paint outerCirclePaint;

    private final int outerCircleRadius;
    private final int innerCircleRadius;

    private boolean isPressed;
    private double dirX;
    private double dirY;
    private double normalizedDirX;
    private double normalizedDirY;
    private double joystickAngle = -90;

    public Joystick(int centerPositionX, int centerPositionY, int outerCircleRadius, int innerCircleRadius) {

        //Outer and inner circle positions
        outerCircleX = centerPositionX;
        outerCircleY = centerPositionY;
        innerCircleX = centerPositionX;
        innerCircleY = centerPositionY;

        //radius of circles
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        //paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.argb(100, 100, 100, 100));
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.argb(150, 100, 100, 100));
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {

        //draw outer circle
        canvas.drawCircle(
                outerCircleX,
                outerCircleY,
                outerCircleRadius,
                outerCirclePaint
        );

        //draw inner circle
        canvas.drawCircle(
                innerCircleX,
                innerCircleY,
                innerCircleRadius,
                innerCirclePaint
        );
    }

    public void update()
    {
        innerCircleX = (int) (outerCircleX + dirX * outerCircleRadius);
        innerCircleY = (int) (outerCircleY + dirY * outerCircleRadius);
    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        //calculating the distance from touch to the center of the joystick using the Pythagoras equation
        double joystickCenterToTouchDistance = Math.sqrt(
                Math.pow(outerCircleX - touchPositionX, 2) +
                        Math.pow(outerCircleY - touchPositionY, 2)
        );
        return joystickCenterToTouchDistance < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setDirection(double touchPositionX, double touchPositionY) {
        //how far away from the center of the joystick, for X and Y separately
        double deltaX = touchPositionX - outerCircleX;
        double deltaY = touchPositionY - outerCircleY;
        double deltaDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        normalizedDirX = deltaX / deltaDistance;
        normalizedDirY = deltaY / deltaDistance;

        //if touching inside the joystick
        if(deltaDistance < outerCircleRadius) {
            dirX = deltaX / outerCircleRadius;
            dirY = deltaY / outerCircleRadius;
        }
        //if touching outside the joystick
        else {
            dirX = normalizedDirX;
            dirY = normalizedDirY;
        }
    }

    public void resetActuator() {
        dirX = 0.0;
        dirY = 0.0;
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }
    public double getNormalizedDirX() {
        return normalizedDirX;
    }

    public double getNormalizedDirY() {
        return normalizedDirY;
    }
    public float getAngle() {
        calculateAngle();
        return (float)joystickAngle;
    }

    public void calculateAngle() {
        if(dirX != 0 && dirY != 0)
        {
            double angle = Math.atan2(dirY, dirX);

            angle = Math.toDegrees(angle);

            if (angle < 0) {
                angle += 360;
            }
            else if(angle > 360)
                angle -= 360;

            joystickAngle = angle;
        }
    }
}
