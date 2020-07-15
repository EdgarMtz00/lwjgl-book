package com.edgarmtz.engine;

import com.edgarmtz.engine.controllers.MouseInput;
import com.edgarmtz.engine.graphics.WindowManager;
import com.edgarmtz.engine.utils.LoopTimer;

public class GameEngine implements Runnable{
    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private final WindowManager window;
    private final LoopTimer timer;
    private final IGameLogic gameLogic;
    private final MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic){
        window = new WindowManager(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        timer = new LoopTimer();
        mouseInput = new MouseInput();
    }

    @Override
    public void run(){
        try{
            init();
            gameLoop();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cleanup();
        }
    }

    protected void init() throws Exception{
        window.init();
        timer.init();
        gameLogic.init();
        mouseInput.init(window);
    }

    //fixed step gameloop
    protected void gameLoop(){
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()){
            elapsedTime  = timer.getElapsedTime();
            accumulator  += elapsedTime;

            input();

            while(accumulator >= interval){
                update(interval);
                accumulator -= interval;
            }

            render();

            if(!window.isVsync()){
                sync();
            }
        }
    }

    private void sync(){
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLooptime() + loopSlot;
        while (timer.getTime() < endTime){
            try{
                Thread.sleep(1);
            }catch (InterruptedException e){
            }
        }

    }

    protected void input(){
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    protected void update(float interval){
        gameLogic.update(interval, mouseInput);
    }

    protected void render(){
        gameLogic.render(window);
        window.update();
    }

    protected  void cleanup(){
        gameLogic.cleanup();
    }
}
