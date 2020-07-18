package com.edgarmtz.engine;

import com.edgarmtz.engine.controllers.MouseInput;
import com.edgarmtz.engine.graphics.WindowManager;

public interface IGameLogic {
    /**
     * Initialize game objects
     * @throws Exception if fails to initialize any object
     */
    void init() throws Exception;

    /**
     * Takes input from mouse and keyboard
     * @param window Provides keyboard functions
     * @param mouseInput Provides mouse functions
     */
    void input(WindowManager window, MouseInput mouseInput);

    /**
     * Updates game data based on input taken
     * @param interval
     * @param mouseInput Stores mouse displacement and clicks
     */
    void update(float interval, MouseInput mouseInput);

    /**
     * Makes a draw call to all game objects
     * @param window Where everything will be drawn
     */
    void render(WindowManager window);

    /**
    * Deletes any temporary data stored in systems memory
    * */
    void cleanup();
}
