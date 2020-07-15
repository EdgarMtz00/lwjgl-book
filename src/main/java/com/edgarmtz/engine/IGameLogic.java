package com.edgarmtz.engine;

import com.edgarmtz.engine.controllers.MouseInput;
import com.edgarmtz.engine.graphics.WindowManager;

public interface IGameLogic {
    void init() throws Exception;

    void input(WindowManager window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);

    void render(WindowManager window);

    void cleanup();
}
