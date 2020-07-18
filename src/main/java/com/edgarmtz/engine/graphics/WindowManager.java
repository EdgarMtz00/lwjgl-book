package com.edgarmtz.engine.graphics;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Manages configuration and operation of window where game will be displayed
 */
public class WindowManager {
    private Long window;
    private int width;
    private  int height;
    private String windowTitle;
    private boolean Vsync;
    private  boolean resized;

    /**
     * Initialize window properties
     * @param windowTitle Text displayed on top of the window
     * @param width Width size taken by the window
     * @param height Height size taken by the window
     * @param Vsync Set on or off vertical synchronization
     */
    public WindowManager(String windowTitle, int width, int height, boolean Vsync){
        this.height = height;
        this.windowTitle = windowTitle;
        this.width = width;
        this.Vsync = Vsync;
        this.resized = false;
    }

    /**
     * Create a window using GLFW and binds it with LWJGL
     */
    public void init(){
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //Ocultar ventana
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); //Habilitar cambio de tamaño
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Indicates GLFW to create a window and stores it's id
        window = glfwCreateWindow(this.width, this.height, this.windowTitle, NULL, NULL);
        if (window == NULL) //NULL forma parte de lwjgl
            throw new RuntimeException("Failed to create window");

        // Window resize Callback
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        // Key pressed on window callback
        glfwSetKeyCallback(window, (window, key, scancode, action, mods)->{
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        // Creates frame to display window
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer pwidth = stack.mallocInt(1);
            IntBuffer pheight = stack.mallocInt(1);

            // Set window size
            glfwGetWindowSize(window, pwidth, pheight);

            // Get monitor resolution
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // place window on center of the screen
            glfwSetWindowSize(
                    window,
                    (vidmode.width()  - pwidth.get(0)) / 2,
                    (vidmode.height()- pheight.get(0) / 2)
            );
        } // New frame will automatically display the window

        // Make the opengl context current
        glfwMakeContextCurrent(window);

        // Enable vsync
        if (Vsync)
            glfwSwapInterval(1);

        // Make window visible
        glfwShowWindow(window);

        // Enable interoperability between GLFW and LWJGL
        GL.createCapabilities();

        // Set window default color
        GL11.glClearColor(1.0f, 0.5f, 1.0f, 0.0f);

        // Establish to render near pixels first
        glEnable(GL_DEPTH_TEST);
    }

    /**
     * Swap gpu image buffer to update windows and allows polling window events
     */
    public void update(){
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /**
     * @return Indicates if window was close
     */
    public boolean windowShouldClose(){
        return glfwWindowShouldClose(window);
    }

    /**
     * Indicates if a certain key is being pressed
     * @param keyCode Key identifier
     * @return isPressed value for that key
     */
    public boolean isKeyPressed(int keyCode){
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    /**
     * Changes window default color
     * @param r Red color value
     * @param g Green color value
     * @param b Blue color value
     * @param a Alpha value
     */
    public void setClearColor(float r, float g, float b, float a){
        GL11.glClearColor(r, g, b, a);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public boolean isVsync() {
        return Vsync;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public Long getWindow() {
        return window;
    }
}
