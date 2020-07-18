package com.edgarmtz.engine.utils;

/**
 * Calculates data from time passed during game loop
 */
public class LoopTimer {
    private double lastLooptime;

    public void init(){
        lastLooptime = getTime();
    }

    /**
     * @return System time in seconds
     */
    public double getTime(){
        return System.nanoTime() / 1000_000_000.0;
    }

    /**
     * @return Time passed since last loop
     */
    public float getElapsedTime(){
        double time = getTime();
        float elapsedTime = (float) (time - lastLooptime);
        lastLooptime = time;
        return  elapsedTime;
    }

    /**
     * @return Time at which last loop started
     */
    public double getLastLooptime(){
        return lastLooptime;
    }
}
