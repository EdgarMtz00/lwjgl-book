package com.edgarmtz.engine.controllers;

import com.edgarmtz.engine.graphics.WindowManager;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

/**
 * Controls mouse events and updates displacement information
 */
public class MouseInput {
    private final Vector2d previousPosition;
    private final Vector2d currentPosition;
    private final Vector2f displacementVec;
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public MouseInput() {
        previousPosition = new Vector2d(-1, -1);
        currentPosition = new Vector2d(0, 0);
        displacementVec = new Vector2f();
    }

    /**
     * Sets Cursor callbacks for position change, enter screen and mouse click
     * @param windowManager Used to get window where callbacks will be set
     */
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

    /**
     * Calculates cursor position change in each axis and updates previous position
     * @param windowManager Todo: delete this useless parameter
     */
    public void input(WindowManager windowManager) {
        displacementVec.x = 0;
        displacementVec.y = 0;
        if(previousPosition.x > 0 && previousPosition.y > 0 && inWindow) {
            double deltaX = currentPosition.x - previousPosition.x;
            double deltaY = currentPosition.y - previousPosition.y;
            boolean rotateX = deltaX != 0;
            boolean rotateY = deltaY != 0;
            if (rotateX)
                displacementVec.x = (float) deltaX;
            if (rotateY)
                displacementVec.y = (float) deltaY;
        }
        previousPosition.x = currentPosition.x;
        previousPosition.y = currentPosition.y;
    }

    public Vector2f getDisplacementVec() {
        return displacementVec;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
