package com.edgarmtz.engine.utils;

public class LoopTimer {
    private double lastLooptime;

    public void init(){
        lastLooptime = getTime();
    }

    public double getTime(){
        return System.nanoTime() / 1000_000_000.0;
    }

    public float getElapsedTime(){
        double time = getTime();
        float elapsedTime = (float) (time - lastLooptime);
        lastLooptime = time;
        return  elapsedTime;
    }

    public double getLastLooptime(){
        return lastLooptime;
    }
}
