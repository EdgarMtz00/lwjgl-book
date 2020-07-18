package com.edgarmtz.engine.graphics;

import com.edgarmtz.engine.entities.Camera;
import com.edgarmtz.engine.entities.GameObject;
import com.edgarmtz.engine.physics.Transformation;
import com.edgarmtz.engine.utils.Resources;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

/**
 * Groups all objects that need to be drawn and controls data used in every draw call made by the engine
 */
public class Renderer {

    private ShaderProgram shaderProgram;
    private static final float FOV = (float) Math.toRadians((60.0f));
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 250.0f;
    private final Transformation transformation;

    public Renderer(){
        transformation = new Transformation();
    }

    /**
     * Loads shaders programs and defines constant data into gpu
     * @throws Exception if shaders programs doesn't exist
     */
    public void init() throws Exception{
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Resources.loadResourceContent("/shaders/VertexShader.vert"));
        shaderProgram.createFragmentShader(Resources.loadResourceContent("/shaders/FragmentShader.frag"));
        shaderProgram.link();
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("color");
        shaderProgram.createUniform("useColor");
    }

    /**
     * Indicates gpu to draw all objects in game according to camera projection and movement
     * @param window Where render will take place
     * @param camera Indicates displacement to put into objects
     * @param gameObjects Array of objects to be drawn
     */
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
            Mesh mesh = gameObject.getMesh();
            Matrix4f worldMatrix = transformation.getModelViewMatrix(gameObject, viewMatrix);
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            shaderProgram.setUniform("color", mesh.getColor());
            shaderProgram.setUniform("useColor", mesh.isTextured() ? 0 : 1);
            mesh.render();
        }

        shaderProgram.unbind();
    }

    /**
     * Clears stuff drawn
     */
    private void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Deletes any temporary data stored in systems memory
     */
    public void cleanup(){
        if(shaderProgram != null){
            shaderProgram.cleanup();
        }
    }
}
