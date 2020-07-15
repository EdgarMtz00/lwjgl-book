package com.edgarmtz.engine.graphics;

import com.edgarmtz.engine.entities.Camera;
import com.edgarmtz.engine.entities.GameObject;
import com.edgarmtz.engine.physics.Transformation;
import com.edgarmtz.engine.utils.Resources;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class Renderer {

    private ShaderProgram shaderProgram;
    private static final float FOV = (float) Math.toRadians((60.0f));
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 5.0f;
    private final Transformation transformation;

    public Renderer(){
        transformation = new Transformation();
    }

    public void init() throws Exception{
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Resources.loadResourceContent("/shaders/VertexShader.vert"));
        shaderProgram.createFragmentShader(Resources.loadResourceContent("/shaders/FragmentShader.frag"));
        shaderProgram.link();
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");
    }

    public void render(WindowManager window, Camera camera, GameObject[] gameObjects){
        clear();

        if(window.isResized()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        shaderProgram.setUniform("texture_sampler", 0);

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        for (GameObject gameObject : gameObjects) {
            Matrix4f worldMatrix = transformation.getModelViewMatrix(gameObject, viewMatrix);
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            gameObject.getMesh().render();
        }

        shaderProgram.unbind();
    }

    private void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        if(shaderProgram != null){
            shaderProgram.cleanup();
        }
    }
}
