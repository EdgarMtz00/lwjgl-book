package com.edgarmtz.engine.controllers;

import com.edgarmtz.engine.graphics.WindowManager;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
    private final Vector2d previousPosition;
    private final Vector2d currentPosition;
    private final Vector2f displVec;
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public MouseInput() {
        previousPosition = new Vector2d(-1, -1);
        currentPosition = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(WindowManager windowManager) {
        GLFW.glfwSetCursorPosCallback(windowManager.getWindow(), (windowHandle, xPosition, yPosition) ->{
            currentPosition.x = xPosition;
            currentPosition.y = yPosition;
        });

        GLFW.glfwSetCursorEnterCallback(windowManager.getWindow(), ((windowHandle, entered) -> inWindow = entered));

        GLFW.glfwSetMouseButtonCallback(windowManager.getWindow(), ((windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        }));
    }

    public void input(WindowManager windowManager) {
        displVec.x = 0;
        displVec.y = 0;
        if(previousPosition.x > 0 && previousPosition.y > 0 && inWindow) {
            double deltaX = currentPosition.x - previousPosition.x;
            double deltaY = currentPosition.y - previousPosition.y;
            boolean rotateX = deltaX != 0;
            boolean rotateY = deltaY != 0;
            if (rotateX)
                displVec.x = (float) deltaX;
            if (rotateY)
                displVec.y = (float) deltaY;
        }
        previousPosition.x = currentPosition.x;
        previousPosition.y = currentPosition.y;
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
