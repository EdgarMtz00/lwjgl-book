package com.edgarmtz.game;

import com.edgarmtz.engine.GameEngine;
import com.edgarmtz.engine.IGameLogic;

public class Main {

    public static void main(String[] args) {
        try {
            boolean Vsync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEngine = new GameEngine("GAME", 600, 480, Vsync, gameLogic);
            gameEngine.run();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
