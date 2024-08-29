package com.example.divedeep;

public class EndGameThread extends Thread
{
    int delay = 0;

    public EndGameThread(int delay)
    {
        this.delay = delay;
    }

    public void run()
    {
        try{
            Thread.sleep(delay);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.exit(0);
    }
}
