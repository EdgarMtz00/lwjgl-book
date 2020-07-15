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

public class WindowManager {
    private Long window;
    private int width;
    private  int height;
    private String windowTitle;
    private boolean Vsync;
    private  boolean resized;

    public WindowManager(String windowTitle, int width, int height, boolean Vsync){
        this.height = height;
        this.windowTitle = windowTitle;
        this.width = width;
        this.Vsync = Vsync;
        this.resized = false;
    }

    public void init(){
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // inicializa api de ventanas
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // configurar api de ventanas
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //Ocultar ventana
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); //Habilitar cambio de tamaño
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // crear ventana
        window = glfwCreateWindow(this.width, this.height, this.windowTitle, NULL, NULL);
        if (window == NULL) //NULL forma parte de lwjgl
            throw new RuntimeException("Failed to create window");

        // callback para el resize
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        // callback para cuando se presione una tecla
        glfwSetKeyCallback(window, (window, key, scancode, action, mods)->{
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        // crear un nuevo frame
        try(MemoryStack stack = MemoryStack.stackPush()){
            // punteros
            IntBuffer pwidth = stack.mallocInt(1);
            IntBuffer pheight = stack.mallocInt(1);

            // pasar el alto y ancho a los punteros
            glfwGetWindowSize(window, pwidth, pheight);

            // obtener la resolucion
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // centrar ventana
            glfwSetWindowSize(
                    window,
                    (vidmode.width()  - pwidth.get(0)) / 2,
                    (vidmode.height()- pheight.get(0) / 2)
            );
        } // el frame metido al stack se muestra automaticamente

        // make the opengl context current
        glfwMakeContextCurrent(window);

        // habilitar vsync
        if (Vsync)
            glfwSwapInterval(1);

        // hacer la ventana visible
        glfwShowWindow(window);

        //unir LWJGL con GLFW y habilitar su interoperacionalidad
        GL.createCapabilities();

        // Set the clear color
        GL11.glClearColor(1.0f, 0.5f, 1.0f, 0.0f);

        //render near pixels first
        glEnable(GL_DEPTH_TEST);
    }


    public void update(){
        glfwSwapBuffers(window); //cambia los buffers de la tarjeta grafica
        glfwPollEvents();// Poll para los eventos de la ventana
    }

    public boolean windowShouldClose(){
        return glfwWindowShouldClose(window);
    }

    public boolean isKeyPressed(int keyCode){
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

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
