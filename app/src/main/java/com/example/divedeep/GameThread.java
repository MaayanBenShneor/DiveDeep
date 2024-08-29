package com.example.divedeep;

import android.os.Build;
import android.view.SurfaceHolder;

import androidx.annotation.RequiresApi;

public class GameThread extends Thread {
    private int FPS = 30;
    private boolean running;
    private GamePanel gamePanel;
    private SurfaceHolder surfaceHolder;
    private long lastFrameTime;
    public static double deltaTime;

    public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }//end of constructor

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;

        long targetTime = 1000 / FPS;

        // if the game is in running mode
        while (running) {
            startTime = System.nanoTime();

            GamePanel.canvas = null;
            //try locking the canvas for pixel editing
            try {
                //we lock canvas to our content view
                GamePanel.canvas = this.surfaceHolder.lockCanvas();

                synchronized (surfaceHolder) {

                    // Calculate delta time
                    long currentFrameTime = System.nanoTime();
                    long elapsedNanoTime = currentFrameTime - lastFrameTime;
                    deltaTime = elapsedNanoTime / 1000000000.0f; // Convert nanoseconds to seconds

                    this.gamePanel.update();
                    this.gamePanel.draw(GamePanel.canvas);

                    lastFrameTime = currentFrameTime;
                }//end synchronized

            } catch (Exception e) {
            }//end try
            finally {
                if (GamePanel.canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(GamePanel.canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Calculate the time it took for the frame
            timeMillis = (System.nanoTime() - startTime) / 1000000;

            // Calculate the time to wait for the next frame
            waitTime = targetTime - timeMillis;
            try {
                this.sleep(waitTime);
            } catch (Exception e) {
            }
        } // end while
    } // end of run method

    public void setRunning(boolean running) {
        this.running = running;
    }
    public static double getDeltaTime()
    {
        return deltaTime;
    }
}
