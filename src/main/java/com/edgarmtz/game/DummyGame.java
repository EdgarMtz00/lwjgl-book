package com.edgarmtz.game;

import com.edgarmtz.engine.IGameLogic;
import com.edgarmtz.engine.controllers.MouseInput;
import com.edgarmtz.engine.entities.Camera;
import com.edgarmtz.engine.entities.GameObject;
import com.edgarmtz.engine.graphics.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private final Renderer renderer;
    private GameObject[] gameObjects;
    private final Vector3f cameraDisplacement;
    private final Camera camera;

    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.2f;

    public DummyGame(){
        renderer = new Renderer();
        camera = new Camera();
        cameraDisplacement = new Vector3f();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
        float[] positions = new float[] {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };

        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };

        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};

        Texture texture = new Texture("/textures/grassblock.png");
        Mesh mesh = ObjLoader.loadMesh("/models/cube.obj");
        mesh.setTexture(texture);
        GameObject gameObject = new GameObject(mesh);

        gameObjects = new GameObject[]{gameObject};
    }

    @Override
    public void input(WindowManager window, MouseInput mouseInput) {
        cameraDisplacement.y = 0;
        cameraDisplacement.x = 0;
        cameraDisplacement.z = 0;

        if ( window.isKeyPressed(GLFW_KEY_Z) )     { cameraDisplacement.y = -1; }
        if ( window.isKeyPressed(GLFW_KEY_X) )   { cameraDisplacement.y = 1; }
        if (window.isKeyPressed(GLFW_KEY_D))        { cameraDisplacement.x = 1; }
        if (window.isKeyPressed(GLFW_KEY_A))        { cameraDisplacement.x = -1; }
        if (window.isKeyPressed(GLFW_KEY_W))        { cameraDisplacement.z = -1; }
        if (window.isKeyPressed(GLFW_KEY_S))        { cameraDisplacement.z = 1; }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
       camera.movePosition(cameraDisplacement.x * CAMERA_POS_STEP,
               cameraDisplacement.y * CAMERA_POS_STEP,
               cameraDisplacement.z * CAMERA_POS_STEP);

       if(mouseInput.isRightButtonPressed()) {
           Vector2f rotationVector = mouseInput.getDisplVec();
           camera.moveRotation(rotationVector.y * MOUSE_SENSITIVITY, rotationVector.x * MOUSE_SENSITIVITY, 0);
       }
    }

    @Override
    public void render(WindowManager window) {
        renderer.render(window, camera, gameObjects);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        for (GameObject gameObject : gameObjects) {
            gameObject.cleanup();
        }
    }
}
